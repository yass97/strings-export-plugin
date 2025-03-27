package org.example

import org.gradle.api.Plugin
import org.gradle.api.Project

class StringsExportPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.tasks.register("exportStrings", StringsExportTask::class.java) {
            it.group = "Strings resource export tasks"
        }
    }
}
