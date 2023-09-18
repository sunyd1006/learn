
# Gradle 教程
https://zhuanlan.zhihu.com/p/27948650
```gradle
plugins {
    id 'java'
}

group 'org.example'
version '1.0-SNAPSHOT'

repositories {
    mavenCentral()
}

dependencies {
    testImplementation 'org.junit.jupiter:junit-jupiter-api:5.7.0'
    testRuntimeOnly 'org.junit.jupiter:junit-jupiter-engine:5.7.0'
}

test {
    useJUnitPlatform()
}

// 指定生成的 包名和版本
jar {
    baseName = 'first-gradle'
    version =  '0.1.0'
}
```