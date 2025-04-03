package io.github

import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import javax.xml.parsers.DocumentBuilderFactory

abstract class StringsExportTask : DefaultTask() {
    @get:InputFile
    abstract val inputFile: RegularFileProperty

    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    @TaskAction
    fun exportStrings() {
        val stringsXml = inputFile.get().asFile
        if (!stringsXml.exists()) {
            logger.error("strings.xml not found")
            return
        }

        val doc = DocumentBuilderFactory.newInstance()
            .newDocumentBuilder()
            .parse(stringsXml)
        val nodeList = doc.getElementsByTagName("string")

        val result = StringBuilder()
        for (i in 0 until nodeList.length) {
            val node = nodeList.item(i)
            val name = node.attributes.getNamedItem("name").nodeValue
            val value = node.textContent
                .replace(Regex("[\\r\\n\\s]"), "")
                .trim()
            result.append("$name\t$value\n")
        }

        val arrayNodeList = doc.getElementsByTagName("string-array")
        for (i in 0 until arrayNodeList.length) {
            val node = arrayNodeList.item(i)
            val name = node.attributes.getNamedItem("name").nodeValue
            for (j in 0 until node.childNodes.length) {
                val childNode = node.childNodes.item(j)
                if (childNode.nodeName == "item") {
                    val value = childNode.textContent.trim()
                    result.append("$name\t$value\n")
                }
            }
        }

        val output = outputFile.get().asFile
        output.parentFile.mkdirs()
        output.writeText(result.toString())

        println("exported: ${output.absolutePath}")
    }
}