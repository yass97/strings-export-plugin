# Strings Export Plugin
A Gradle plugin that exports Android string resources (`strings.xml`) into a tab-separated format for use in spreadsheets or localization tools.

## Features
- Parses `strings.xml` and outputs a `.tsv` file
- Spreadsheet-friendly output

## Installation
To use Strings Export Plugin in your project, add the following to your `settings.gradle.kts` and `build.gradle.kts` file:

> settings.gradle.kts
```kotlin
pluginManagement {
    repositories {
        gradlePluginPortal()
    }
}
```
> build.gradle.kts
```kotlin
plugins {
    id("io.github.yass97.strings.export") version "1.0.1"
}
```

## Usage
Run the following Gradle task to generate the tsv file:

```bash
./gradlew exportStrings
```

### Output destination
Output `strings-export.tsv` to the module `build` directory:

```
build/strings-export.tsv
```

### Output format
```
id<TAB>string
```

## Example

### Basic string resources
> strings.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="key1">message1</string>
    <string name="key2">message2</string>
</resources>
```
> output
```
key1	message1
key2	message2
```

### String array
> strings.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string-array name="key1">
        <item>item1</item>
        <item>item2</item>
    </string-array>
</resources>
```
> output
```
key1	item1
key1	item2
```

### Multiline strings
> strings.xml
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="key1">
        line1
        line2
        line3
    </string>
</resources>
```
> output
```
key1	line1line2line3
```
