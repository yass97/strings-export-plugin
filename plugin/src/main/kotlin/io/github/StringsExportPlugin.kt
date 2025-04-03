package io.github

import org.gradle.api.Plugin
import org.gradle.api.Project

class StringsExportPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("exportStrings", StringsExportTask::class.java) {
            it.group = "Strings resource export tasks"
            it.inputFile.set(it.project.layout.projectDirectory.file("src/main/res/values/strings.xml"))
            it.outputFile.set(it.project.layout.buildDirectory.file("strings-export.tsv"))
        }
    }
}
