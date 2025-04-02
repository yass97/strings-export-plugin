package io.github

import org.gradle.testfixtures.ProjectBuilder
import kotlin.test.Test
import kotlin.test.assertNotNull

class StringsExportPluginTest {
    @Test fun `plugin registers task`() {
        val project = ProjectBuilder.builder().build()
        project.plugins.apply("com.yass.strings.export")

        assertNotNull(project.tasks.findByName("exportStrings"))
    }
}
