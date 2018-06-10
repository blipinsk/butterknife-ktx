package com.bartoszlipinski.butterknifektx

import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.reflections.Reflections
import org.reflections.scanners.SubTypesScanner
import org.reflections.scanners.TypeAnnotationsScanner
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder

@RunWith(JUnit4::class)
class ButterKnifeKtxProcessorTest {

    @Test
    fun `getSupportedAnnotationTypes contains all annotations from butterknife`() {
        //given
        val butterknifeMainPkg = "butterknife"
        val butterknifeReflections = Reflections(ConfigurationBuilder()
                .setUrls(ClasspathHelper.forPackage(butterknifeMainPkg))
                .setScanners(SubTypesScanner(false), TypeAnnotationsScanner()))
        val allTypes =
                butterknifeReflections.getSubTypesOf(Any::class.java)
        val butterknifeAnnotations = allTypes
                .filter { it.`package`.name == butterknifeMainPkg }
                .filter { it.isAnnotation }
                .map { it.canonicalName }
                .toList()

        //when
        val supportedAnnotations = ButterKnifeKtxProcessor().supportedAnnotationTypes

        //then
        assertThat(supportedAnnotations).containsExactlyElementsIn(butterknifeAnnotations)
    }
}