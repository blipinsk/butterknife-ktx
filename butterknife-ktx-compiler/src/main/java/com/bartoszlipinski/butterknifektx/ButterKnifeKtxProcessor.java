/*
 * Copyright 2018 Bartosz Lipinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bartoszlipinski.butterknifektx;

import android.support.annotation.NonNull;

import com.bartoszlipinski.butterknifektx.utils.Logger;
import com.bartoszlipinski.butterknifektx.utils.Utils;
import com.google.auto.service.AutoService;
import com.squareup.kotlinpoet.FileSpec;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;

import butterknife.compiler.ButterKnifeProcessor;

import static com.bartoszlipinski.butterknifektx.utils.Utils.kotlinFiler;
import static java.util.Collections.singleton;
import static javax.lang.model.SourceVersion.latestSupported;

@AutoService(Processor.class)
public class ButterKnifeKtxProcessor extends AbstractProcessor {

    private static final String FILE_GENERATION_COMMENT = "Generated code from butterknife-ktx compiler. Do not modify!";
    private static final String PACKAGE_BUTTERKNIFE_KTX = "com.bartoszlipinski.butterknifektx";
    private static final String KTX_SUFFIX = "$$Ktx";

    private static final Set<String> SUPPORTED_ANNOTATION_TYPES = new ButterKnifeProcessor()
            .getSupportedAnnotationTypes();

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        Logger.initialize(processingEnv);
        Utils.initialize(processingEnv);
    }

    @Override
    public Set<String> getSupportedOptions() {
        return singleton(Utils.KotlinFiler.KAPT_KOTLIN_GENERATED_OPTION_NAME);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return SUPPORTED_ANNOTATION_TYPES;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnv) {
        try {
            Set<Element> annotatedElements = getAllAnnotatedElements(roundEnv);
            Set<ViewBindingCandidate> candidates = new HashSet<>();
            for (Element annotatedElement : annotatedElements) {
                Element enclosingElement = annotatedElement.getEnclosingElement();
                addCandidateIfNeeded(candidates, enclosingElement);
            }
            generateExtensionBridge(candidates);
        } catch (IOException e) {
            Logger.error("Error while creating a file: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            Logger.error("Couldn't find Annotation class " + e.getMessage());
        }
        return false; //not claiming the annotation
    }

    private void addCandidateIfNeeded(Set<ViewBindingCandidate> candidates,
                                      Element enclosingElement) {
        ViewBindingCandidate newCandidate = new ViewBindingCandidate((TypeElement) enclosingElement);
        boolean foundCandidate = false;
        for (ViewBindingCandidate cachedCandidate : candidates) {
            if (cachedCandidate.getQualifiedName().equals(newCandidate.getQualifiedName())) {
                foundCandidate = true;
                break;
            }
        }
        if (!foundCandidate) {
            candidates.add(newCandidate);
        }
    }

    @NonNull
    private Set<Element> getAllAnnotatedElements(RoundEnvironment roundEnv) throws ClassNotFoundException {
        Set<Element> annotatedElements = new HashSet<>();
        for (String supportedAnnotationType : SUPPORTED_ANNOTATION_TYPES) {
            @SuppressWarnings("unchecked") Class<? extends Annotation> classOfAnnotation =
                    (Class<? extends Annotation>) Class.forName(supportedAnnotationType);
            annotatedElements.addAll(roundEnv.getElementsAnnotatedWith(classOfAnnotation));
        }
        return annotatedElements;
    }

    private void generateExtensionBridge(Set<ViewBindingCandidate> candidates) throws IOException {
        KotlinCodeGenerator generator = new KotlinCodeGenerator();
        for (ViewBindingCandidate candidate : candidates) {
            FileSpec.Builder builder = FileSpec.builder(PACKAGE_BUTTERKNIFE_KTX,
                    candidate.getViewBindingCanonicalName() + KTX_SUFFIX)
                    .addComment(FILE_GENERATION_COMMENT);

            switch (candidate.bindingType()) {
                case ACTIVITY_OR_DIALOG: // generate bind(target: Activity) or bind(target: Dialog)
                    builder = builder.addFunction(generator.generateBindActivityOrDialog(candidate));
                    break;
                case VIEW: // generate bind(target: View)
                    builder = builder.addFunction(generator.generateBindView(candidate));
                    break;
                case OTHER: // generate bind(target: Any, source: Activity), bind(target: Any, source: View), bind(target: Any, source: Dialog)
                    builder = builder
                            .addFunction(generator.generateBindTargetActivity(candidate))
                            .addFunction(generator.generateBindTargetView(candidate))
                            .addFunction(generator.generateBindTargetDialog(candidate));
                    break;
                default:
                    throw new IllegalStateException("Unhandled bindingType");
            }

            builder.build().writeTo(kotlinFiler());
        }
    }
}