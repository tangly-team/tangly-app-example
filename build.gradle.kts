import org.gradle.api.tasks.testing.logging.TestExceptionFormat

plugins {
    java
    application
    alias(libs.plugins.vaadin)
}

defaultTasks("clean", "build")

repositories {
    mavenLocal()
    mavenCentral()
    maven(url = "https://maven.vaadin.com/vaadin-addons")
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(22))
    withJavadocJar()
    withSourcesJar()
}

dependencies {
    // Vaadin
    implementation(libs.vaadin.core) {
        if (vaadin.effective.productionMode.get()) {
            exclude(module = "vaadin-dev")
        }
    }

    implementation(libs.tangly.apps)
    implementation(libs.tangly.commons)
    implementation(libs.tangly.core)
    implementation(libs.tangly.ui)

    // Vaadin-Boot
    implementation(libs.vaadin.boot)
    implementation(libs.jetbrains.annotations)
    implementation(libs.tangly.apps)
    implementation(libs.tangly.core)
    implementation(libs.tangly.ui)
    implementation(libs.javalin) {
        exclude(group = "org.eclipse.jetty")
        exclude(group = "org.eclipse.jetty.websocket")
        exclude(group = "com.fasterxml.jackson.core")
    }

    implementation(libs.apache.log4j2.api)

    implementation(libs.vaadin.pdfviewer)
    implementation(libs.vaadin.socharts)
    implementation(libs.javalin.openapi)
    implementation(libs.javalin.swagger)
    annotationProcessor(libs.javalin.openapi.processor)

    testImplementation(libs.kaributesting)
    testImplementation(libs.junit.jupiter.engine)
    testRuntimeOnly(libs.junit.platform.launcher)
}

java {
    sourceCompatibility = JavaVersion.VERSION_22
    targetCompatibility = JavaVersion.VERSION_22
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("--enable-preview")
}

tasks.withType<JavaExec>().configureEach {
    jvmArgs("--enable-preview")
}
tasks.withType<Test>().configureEach {
    useJUnitPlatform()
    testLogging {
        exceptionFormat = TestExceptionFormat.FULL
    }
    jvmArgs("--enable-preview")

}

application {
    mainClass = "net.tangly.app.ui.Main"
}
