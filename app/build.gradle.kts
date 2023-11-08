plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("jacoco")
}

jacoco {
    toolVersion = "0.8.10" // Use the appropriate version of Jacoco
}

android {
    namespace = "com.example.newroomdatabase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.newroomdatabase"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )



        }

        debug {
            testCoverage {
                isDebuggable = true
                enableUnitTestCoverage = true
                enableAndroidTestCoverage = true
            }
        }
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    testOptions {

        unitTests {
            isIncludeAndroidResources = true
            isReturnDefaultValues = true
        }
    }
    (this as? Test)?.configure<ExtensionContainer> {
        // Define which tasks should be covered by JaCoCo
        extensions.getByType(JacocoTaskExtension::class).apply {
            isIncludeNoLocationClasses = true

        }
    }
    packagingOptions {

        resources {
            excludes.add ("META-INF/LICENSE-notice.md")
            excludes.add("META-INF/LICENSE.txt")
            excludes.add("META-INF/LICENSE")
            excludes.add("META-INF/NOTICE.txt")
            excludes.add("com/fasterxml/jackson/core/json/VERSION.txt")
            excludes.add("com/fasterxml/jackson/core/Base64Variant")
            excludes.add("com/google/api/client/http/AbstractHttpContent.class")
            excludes.add("thirdpartynotice.txt")
            excludes.add("simplelogger.properties")
            excludes.add("META-INF/spring.factories")
            excludes.add("META-INF/license.txt")
            excludes.add("META-INF/DEPENDENCIES")
            excludes.add("META-INF/LICENSE.md")
            excludes.add("META-INF/NOTICE.md")
            excludes.add("META-INF/spring.schemas")
            excludes.add("META-INF/spring.tooling")
            excludes.add("META-INF/spring.handlers")
            excludes.add("META-INF/spring-configuration-metadata.json")
            excludes.add("META-INF/additional-spring-configuration-metadata.json")
            excludes.add("META-INF/notice.txt")
            pickFirsts.add("META-INF/INDEX.LIST")
            pickFirsts.add("META-INF/io.netty.versions.properties")

            resources.excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}


tasks.register<JacocoReport>("jacocoTestReport")  {
    dependsOn("testDebugUnitTest", "createDebugCoverageReport")

    reports {
        xml.required.set(true)
        html.required.set(true)
        html.outputLocation.set(file("$buildDir/reports/jacoco/html"))
    }


    val fileFilter = listOf(
        "**/R.class",
        "**/R$*.class",
        "**/BuildConfig.*",
        "**/Manifest*.*",
        "**/*Test*.*",
        "android/**/*.*"
    )

    val debugTree = fileTree(mapOf("dir" to "${project.buildDir}/intermediates/classes/debug", "excludes" to fileFilter))
    val mainSrc = "${project.projectDir}/src/main/java"

    sourceDirectories.setFrom(files(mainSrc))
    classDirectories.setFrom(files(debugTree))
    executionData.setFrom(fileTree(mapOf("dir" to buildDir, "includes" to listOf(
        "jacoco/testDebugUnitTest.exec",
        "outputs/code-coverage/connected/*coverage.ec"
    ))))
}


dependencies {

    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    implementation("androidx.test.espresso:espresso-contrib:3.5.1")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.3")
    ksp("androidx.room:room-compiler:2.6.0")

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.6.2")
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.6.2")
    annotationProcessor ("androidx.lifecycle:lifecycle-compiler:2.6.2")

    testImplementation ("com.google.truth:truth:1.0.1")
    androidTestImplementation ("com.google.truth:truth:1.0.1")

    implementation("org.jacoco:org.jacoco.core:0.8.10")

    implementation("org.mockito:mockito-junit-jupiter:5.7.0")
//    testImplementation ("org.mockito:mockito-core:2.13.0")
//    testImplementation ("org.mockito:mockito-inline:4.5.1")
//    testImplementation ("org.mockito.kotlin:mockito-kotlin:4.1.0")

//    debugImplementation ("org.jacoco:org.jacoco.agent:0.8.10")
//    releaseImplementation ("org.jacoco:org.jacoco.agent:0.8.10")

//    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.0")
//    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.0")

   //4 files found with path 'META-INF/LICENSE.md'.
    //Adding a packagingOptions block may help, please refer to
    //https://developer.android.com/reference/tools/gradle-api/8.1/com/android/build/api/dsl/ResourcesPackagingOptions
    //for more information
}





