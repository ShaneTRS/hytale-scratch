plugins {
    id("java")
}

group = "trs.plugin"
version = "0.2.0"

repositories {
    mavenCentral()
    maven("https://cursemaven.com")
}

dependencies {
    compileOnly(files("libs/HytaleServer.jar"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}