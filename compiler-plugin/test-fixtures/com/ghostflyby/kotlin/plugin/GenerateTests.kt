package com.ghostflyby.kotlin.plugin

import com.ghostflyby.kotlin.plugin.runners.AbstractJvmBoxTest
import org.jetbrains.kotlin.generators.generateTestGroupSuiteWithJUnit5

fun main() {
    generateTestGroupSuiteWithJUnit5 {
        testGroup(testDataRoot = "compiler-plugin/testData", testsRoot = "compiler-plugin/test-gen") {
            testClass<AbstractJvmBoxTest> {
                model("box")
            }
        }
    }
}
