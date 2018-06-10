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

import com.bartoszlipinski.butterknifektx.utils.getPackage
import com.squareup.kotlinpoet.ClassName
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.NoType


const val VIEW_BINDING_SUFFIX = "_ViewBinding"

class ViewBindingCandidate(val element: TypeElement) {

    val qualifiedName = element.qualifiedName.toString()

    val packageName: String = element.getPackage()

    val viewBindingSimpleName by lazy {
        var baseName = element.qualifiedName.toString()
                .substring(packageName.length + 1)
                .replace('.', '$') + VIEW_BINDING_SUFFIX
        if (baseName.contains('\$')) {
            "`$baseName`" //surrounding with backticks ``
        } else {
            baseName
        }
    }

    val viewBindingCanonicalName by lazy {
        "$packageName.$viewBindingSimpleName"
    }

    val targetClassName by lazy {
        ClassName.bestGuess(qualifiedName)
    }

    val initializedClassName by lazy {
        ClassName(packageName, viewBindingSimpleName)
    }

    fun bindingType(): BindingType {
        var superType = element.superclass
        while (superType !is NoType) {
            var typeElement = (superType as DeclaredType).asElement() as TypeElement
            return when (typeElement.qualifiedName.toString()) {
                "android.app.Activity", "android.app.Dialog" -> BindingType.ACTIVITY_OR_DIALOG
                "android.view.View" -> BindingType.VIEW
                else -> BindingType.OTHER
            }
        }
        throw IllegalStateException("Unhandled element: $element.qualifiedName")
    }

    enum class BindingType {
        ACTIVITY_OR_DIALOG,
        VIEW,
        OTHER
    }
}