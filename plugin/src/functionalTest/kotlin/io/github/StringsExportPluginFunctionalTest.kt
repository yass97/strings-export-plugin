package io.github

import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.io.TempDir
import java.io.File
import java.io.IOException
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class StringsExportPluginFunctionalTest {

    @field:TempDir
    lateinit var projectDir: File

    private val buildFile by lazy { projectDir.resolve("build.gradle") }
    private val settingsFile by lazy { projectDir.resolve("settings.gradle") }
    private val outputFile by lazy { File("$projectDir/build/strings-export.tsv") }
    private val runner by lazy { GradleRunner.create() }

    @BeforeTest
    fun setup() {
        settingsFile.writeText("")
        buildFile.writeText(
            """
                plugins {
                    id('io.github.yass97.strings.export')
                }
            """.trimIndent()
        )
        runner.forwardOutput()
            .withPluginClasspath()
            .withArguments("exportStrings")
            .withProjectDir(projectDir)
    }

    @Test
    fun `can run task`() {
        val resource =
            """
                <?xml version="1.0" encoding="utf-8"?>
                <resources>
                    <string name="key">message</string>
                </resources>
            """.trimIndent()
        writeResources(resource)

        runner.build()

        assertEquals("key\tmessage\n", outputFile.readText())
    }

    @Test
    fun `two results output when two resources exist`() {
        val resource =
            """
                <?xml version="1.0" encoding="utf-8"?>
                <resources>
                    <string name="key1">message1</string>
                    <string name="key2">message2</string>
                </resources>
            """.trimIndent()
        writeResources(resource)

        runner.build()

        val expected = StringBuilder()
            .append("key1\tmessage1\n")
            .append("key2\tmessage2\n")
            .toString()
        assertEquals(expected, outputFile.readText())
    }

    @Test
    fun `can output string-array tag resources`() {
        val resource =
            """
                <?xml version="1.0" encoding="utf-8"?>
                <resources>
                    <string name="key1">message1</string>
                    <string-array name="key2">
                        <item>item1</item>
                        <item>item2</item>
                    </string-array>
                </resources>
            """.trimIndent()
        writeResources(resource)

        runner.build()

        val expected = StringBuilder()
            .append("key1\tmessage1\n")
            .append("key2\titem1\n")
            .append("key2\titem2\n")
            .toString()
        assertEquals(expected, outputFile.readText())
    }

    @Test
    fun `fails Task when the strings file does not exist`() {
        val result = runner.build()
        assertTrue(result.output.contains("strings.xml not found"))
    }

    @Test
    fun `output on a single line when there is a new line`() {
        val resource =
            """
                <?xml version="1.0" encoding="utf-8"?>
                <resources>
                    <string name="key1">
                        line1
                        line2
                            line3
                    </string>
                </resources>
            """.trimIndent()
        writeResources(resource)

        runner.build()

        val expected = "key1\tline1line2line3\n"
        println("expected=$expected")
        println("output=${outputFile.readText()}")

        assertEquals(expected, outputFile.readText())
    }

    private fun writeResources(resource: String) {
        val stringsXml = File("${projectDir}/src/main/res/values/strings.xml")
        if (!stringsXml.parentFile.mkdirs()) {
            fail("Failed to create strings.xml file")
        }
        try {
            stringsXml.createNewFile()
        } catch (e: IOException) {
            fail("Failed to create strings.xml file. ${e.message}")
        }

        stringsXml.outputStream().use {
            it.write(resource.toByteArray())
        }
    }
}
