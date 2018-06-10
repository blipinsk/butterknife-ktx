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
package com.bartoszlipinski.butterknifektx

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FunSpec

class KotlinCodeGenerator {

    companion object {
        private const val FUN_BIND = "bind"
        private const val PARAM_TARGET = "target"
        private const val PARAM_SOURCE_VIEW = "sourceView"
        private const val PARAM_SOURCE = "source"

        private const val STATEMENT_GET_DECOR_VIEW = "val %L = %L.getWindow().getDecorView()"
        private const val STATEMENT_RETURN_NEW_INSTANCE = "return %T(%L, %L)"

        private val CLASS_NAME_UNBINDER =
                ClassName("butterknife", "Unbinder")
        private val CLASS_NAME_BUTTERKNIFE_KTX =
                ClassName("com.bartoszlipinski.butterknifektx", "ButterKnifeKtx")
        private val CLASS_NAME_ACTIVITY =
                ClassName("android.app", "Activity")
        private val CLASS_NAME_VIEW =
                ClassName("android.view", "View")
        private val CLASS_NAME_DIALOG =
                ClassName("android.app", "Dialog")

        private val ANNOTATION_UI_THREAD = AnnotationSpec
                .builder(ClassName("android.support.annotation", "UiThread"))
                .build()
    }

    fun generateBindActivityOrDialog(candidate: ViewBindingCandidate): FunSpec {
        return FunSpec.builder(FUN_BIND)
                .addAnnotation(ANNOTATION_UI_THREAD)
                .returns(CLASS_NAME_UNBINDER)
                .receiver(CLASS_NAME_BUTTERKNIFE_KTX)
                .addParameter(PARAM_TARGET, candidate.targetClassName)
                .addStatement(STATEMENT_GET_DECOR_VIEW, PARAM_SOURCE_VIEW, PARAM_TARGET)
                .addStatement(STATEMENT_RETURN_NEW_INSTANCE,
                        candidate.initializedClassName, PARAM_TARGET, PARAM_SOURCE_VIEW)
                .build()
    }

    fun generateBindView(candidate: ViewBindingCandidate): FunSpec {
        return FunSpec.builder(FUN_BIND)
                .addAnnotation(ANNOTATION_UI_THREAD)
                .returns(CLASS_NAME_UNBINDER)
                .receiver(CLASS_NAME_BUTTERKNIFE_KTX)
                .addParameter(PARAM_TARGET, candidate.targetClassName)
                .addStatement(STATEMENT_RETURN_NEW_INSTANCE,
                        candidate.initializedClassName, PARAM_TARGET, PARAM_TARGET)
                .build()
    }

    fun generateBindTargetActivity(candidate: ViewBindingCandidate): FunSpec {
        return FunSpec.builder(FUN_BIND)
                .addAnnotation(ANNOTATION_UI_THREAD)
                .returns(CLASS_NAME_UNBINDER)
                .receiver(CLASS_NAME_BUTTERKNIFE_KTX)
                .addParameter(PARAM_TARGET, candidate.targetClassName)
                .addParameter(PARAM_SOURCE, CLASS_NAME_ACTIVITY)
                .addStatement(STATEMENT_GET_DECOR_VIEW, PARAM_SOURCE_VIEW, PARAM_SOURCE)
                .addStatement(STATEMENT_RETURN_NEW_INSTANCE,
                        candidate.initializedClassName, PARAM_TARGET, PARAM_SOURCE_VIEW)
                .build()
    }

    fun generateBindTargetView(candidate: ViewBindingCandidate): FunSpec {
        return FunSpec.builder(FUN_BIND)
                .addAnnotation(ANNOTATION_UI_THREAD)
                .returns(CLASS_NAME_UNBINDER)
                .receiver(CLASS_NAME_BUTTERKNIFE_KTX)
                .addParameter(PARAM_TARGET, candidate.targetClassName)
                .addParameter(PARAM_SOURCE, CLASS_NAME_VIEW)
                .addStatement(STATEMENT_RETURN_NEW_INSTANCE,
                        candidate.initializedClassName, PARAM_TARGET, PARAM_SOURCE)
                .build()
    }

    fun generateBindTargetDialog(candidate: ViewBindingCandidate): FunSpec {
        return FunSpec.builder(FUN_BIND)
                .addAnnotation(ANNOTATION_UI_THREAD)
                .returns(CLASS_NAME_UNBINDER)
                .receiver(CLASS_NAME_BUTTERKNIFE_KTX)
                .addParameter(PARAM_TARGET, candidate.targetClassName)
                .addParameter(PARAM_SOURCE, CLASS_NAME_DIALOG)
                .addStatement(STATEMENT_GET_DECOR_VIEW, PARAM_SOURCE_VIEW, PARAM_SOURCE)
                .addStatement(STATEMENT_RETURN_NEW_INSTANCE,
                        candidate.initializedClassName, PARAM_TARGET, PARAM_SOURCE_VIEW)
                .build()
    }
}

