package org.example

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class StringsExportTask : DefaultTask() {
    @TaskAction
    fun exportStrings() {
        println("start strings export")
    }
}