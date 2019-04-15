# Jokster App :japanese_goblin: 

A simple joke-telling app demonstrating the very basics of [Dagger2](https://google.github.io/dagger/). :dagger:

## Motivation

[Dependency injection][1] or DI is a [25-dollar term for a 5-cent concept][3]. It means giving an object its instance variables. So instead of e.g. creating an `OkHttpClient` instance yourself: 


```kotlin
class ChuckApi {
  private val client = OkHttpClient()
  // ...
}
```

_inject_ it via a constructor: 

```kotlin
class ChuckApi(private val client: OkHttpClient) { 
 // ...
}
```

Voilà! You just performed a _dependency injection_. Simple, right?

<p align="center">
  <img src="https://media.giphy.com/media/1M9fmo1WAFVK0/giphy.gif"/>
</p>

One of the cornerstones of [object-oriented design][5] is the [Dependency inversion principle][4]. In essence, it focuses on _what_ the code does, and not _how_ it does it. Consider the following class:

```kotlin
class ChuckApi {
  fun tellJoke(): String {
    // returns a funny joke from the interwebs
  }
}
```

If another class relies on the `ChuckApi`, such as:

```kotlin
class Repo(private val api: ChuckApi) {
 // ...
}
```

then any changes to the `ChuckApi` most likely affect the `Repo`, too. Also, assuming `ChuckApi` requires a network connection, `Repo` might be difficult to test. Introducing an interface such as:

```kotlin
interface FunnyApi {
  fun tellJoke(): String
}
``` 

and having `ChuckApi` implement it: 

```kotlin
class ChuckApi : FunnyApi {
  override fun tellJoke(): String {
    // same as before
  }
}
```

but making the `Repo` depend on the interface, rather than a concrete class:

```kotlin
class Repo(private val api: FunnyApi) {
 // heavy use of the api here ...
}
``` 

gives us the ability to pass `Repo` any implementation of the `FunnyApi` interface. This makes testing easier and ensures `Repo` needs no code changes across various implementations.

So, how do we pass `FunnyApi` instances to the `Repo` class then? You guessed it - it's with dependency injection! 

## A super-duper-srsly-vague intro to Dagger2

Dagger2 is a popular DI framework for Java, Kotlin and Android. To use it, first add it to the list of your dependencies in `build.gradle`:

```groovy
dependencies {
  implementation 'com.google.dagger:dagger:2.x'
  annotationProcessor 'com.google.dagger:dagger-compiler:2.x'
}
```

When using Kotlin, use `kapt` instead: 

```groovy
apply plugin: 'kotlin-kapt'

dependencies {
  implementation 'com.google.dagger:dagger:2.x'
  kapt 'com.google.dagger:dagger-compiler:2.x'
}
```

To provide dependencies, you need _modules_. Modules are classes with the `@Module` annotation, and they include `@Provides` methods:

```kotlin
@Module  
class NetworkModule {  
    @Provides fun providesHttpClient(): OkHttpClient = OkHttpClient()
}
```

Dagger will inspect the class, look at the return types of the methods annotated with `@Provides` and say: _I now know how to create these objects!_

Another method might need an `OkHttpClient` to create an instance:

```kotlin
@Module  
class ApiModule {  
    @Provides fun providesFunnyApi(c: OkHttpClient): FunnyApi = FunnyApiImpl(c)
}
```

Assuming Dagger knows about the `NetworkModule`, it knows how to create the `OkHttpClient` and therefore knows how to create an instance of `FunnyApi`. In cases where Dagger can infer all the necessary dependencies, you can use a shorter [`@Binds`][6] annotation:

```kotlin
@Module  
interface ApiModule {  
    // exactly the same as the ApiModule above, but shorter. 
    @Binds fun providesFunnyApi(api: FunnyApiImpl): FunnyApi
}
```

To let Dagger create objects on your behalf, you need to add an `@Inject` annotation to your constructors:

```kotlin
class FunnyApiImpl @Inject constructor(c: OkHttpClient) : FunnyApi {  
    // ...
}
```

_Components_ stitch multiple modules together:

```kotlin
@Component(  
    modules = [  
        NetworkModule::class,  
        ApiModule::class  
  ]  
)  
interface AppComponent {  
    fun inject(activity: MainActivity)
    fun client(): OkHttpClient
}
```

Note the `@Component` annotation, along with the list of modules this component includes. 

Dagger will generate a class called `DaggerAppComponent` implementing the `AppComponent` interface. To consume the dependencies, you need a `DaggerAppComponent` instance, which is typically created in an `Application` class:

```kotlin
class App : Application() {  
    private lateinit var component: AppComponent  
  
    override fun onCreate() {  
        super.onCreate()  
        component = DaggerAppComponent.builder()  
            .networkModule(NetworkModule())  
            .apiModule(ApiModule())  
            .build()  
    }
  
    fun component(): AppComponent = component  
}
```

You're now ready to consume dependencies. There are two ways to do it.

1. Use an `@Inject` annotation:

```kotlin
class MainActivity : AppCompatActivity() {  
    @Inject lateinit var factory: JokerViewModel.Factory  
  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  

        // get the AppComponent instance, and invoke the 
        // appropriate inject method which will automatically
        // populate all @Inject fields.
        val component = (application as App).component()  
        component.inject(this)
    }
}
```

2. Call the appropriate accessor methods:

```kotlin
class MainActivity : AppCompatActivity() {    
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  

        // get the AppComponent instance and invoke the getter.
        val component = (application as App).component()  
        val client = component.client()
    }
}
```

Use this repo to play around with Dagger2, and check out these links to learn more: 
- [history and motivation for Dagger2][7]
- [a longer & better intro to Dagger2 by Jake Wharton][8]
- [official docs][9]

## License 

    Copyright © 2019 tslamic
    This work is free. You can redistribute it and/or modify it under the
    terms of the Do What The Fuck You Want To Public License, Version 2,
    as published by Sam Hocevar.

[1]: https://stackoverflow.com/a/1638961/905349
[2]: https://github.com/EnterpriseQualityCoding/FizzBuzzEnterpriseEdition
[3]: https://www.jamesshore.com/Blog/Dependency-Injection-Demystified.html
[4]: https://en.wikipedia.org/wiki/Dependency_inversion_principle
[5]: https://en.wikipedia.org/wiki/Object-oriented_design
[6]: https://google.github.io/dagger/faq.html#why-is-binds-different-from-provides
[7]: https://youtu.be/oK_XtfXPkqw
[8]: https://youtu.be/plK0zyRLIP8
[9]: https://google.github.io/dagger/