# CommentCard
This is a modern Android application built entirely with Kotlin and Jetpack Compose. It serves as a demonstration of modern Android development best practices, focusing on a clean, scalable, and state-driven architecture. The application fetches a list of comments from a public API and displays them in a responsive and interactive UI.

## Features

- **100% Kotlin & Jetpack Compose**: The entire UI is built with Jetpack Compose, using no XML layouts.
- **Responsive UI**: The layout adapts to different screen sizes (Compact, Medium, Expanded) and orientations (Portrait, Landscape).
- **API Integration**: Fetches data from the [JSONPlaceholder](https://jsonplaceholder.typicode.com/) public REST API using Retrofit.
- **State-Driven UI**: Follows a unidirectional data flow (UDF) pattern, where UI state is observed and rendered by Composables.
- **Image Loading**: Asynchronously loads and displays profile pictures with [Coil](https://coil-kt.github.io/coil/).
- **Interactive Elements**:
    - Users can tap a profile image to launch the system image picker and replace it.
    - Long comments are truncated with a "Show more" / "Show less" toggle.
- **Robust Error Handling**: Displays user-friendly error messages for network failures (e.g., no connectivity, timeouts).
- **Dependency Injection**: Uses Hilt for managing dependencies throughout the application.
- **Comprehensive Testing**: Includes unit tests for ViewModels and Repositories.

## Architecture

This project follows a state-of-the-art architecture designed for scalability and maintainability. It is grounded in the principles of **Clean Architecture** and uses the **Model-View-Intent (MVI)** pattern for the UI layer.

- **UI Layer (`ui`)**: Built with Jetpack Compose. `ViewModel`s expose a single `StateFlow` of the UI state, which the Composables observe. User actions are sent to the ViewModel as `Event`s. This creates a predictable, unidirectional data flow.
- **Data Layer (`data`)**: Contains the `Repository`, remote data sources (`APIService`), and data models. The repository is the single source of truth for all application data and abstracts the data sources from the rest of the app.
- **Dependency Injection (`di`)**: Hilt is used to provide dependencies where they are needed, decoupling components and simplifying the object graph.

## Tech Stack & Key Libraries

- **UI**: [Jetpack Compose](https://developer.android.com/jetpack/compose) with [Material 3](https://m3.material.io/)
- **Architecture**: [MVI](https://www.google.com/search?q=https://developer.android.com/topic/architecture/ui-layer%23udf), [MVVM](https://www.google.com/search?q=https://developer.android.com/topic/architecture/viewmodel), [Clean Architecture Principles](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- **Asynchronous Programming**: [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) and [Flow](https://developer.android.com/kotlin/flow)
- **Dependency Injection**: [Hilt](https://developer.android.com/training/dependency-injection/hilt-android)
- **Networking**: [Retrofit 2](https://square.github.io/retrofit/) for REST API communication & [OkHttp 4](https://square.github.io/okhttp/) as the HTTP client.
- **JSON Parsing**: [Moshi](https://github.com/square/moshi)
- **Image Loading**: [Coil 3](https://coil-kt.github.io/coil/)
- **Testing**: [JUnit 4](https://junit.org/junit4/) & [Mockito](https://site.mockito.org/)

## Setup

To build and run the project, follow these steps:

1.  **Clone the repository**:
    ```sh
    git clone git@github.com:lovekotlin/CommentCard.git
    ```
2.  **Open in Android Studio**:
    Open the project in the latest stable version of Android Studio.
3.  **Build the project**:
    Android Studio should automatically sync the Gradle files and download the required dependencies. Click the "Run" button to build and deploy the app to an emulator or a physical device.

## Running Tests

This project includes unit tests to ensure the reliability of its business logic.

- **Unit Tests**: Located in the `/app/src/test/` directory. These tests cover the `CommentsViewModel` and `CommentsRepository`.
- **Running from Android Studio**:
    1.  Right-click on the `/app/src/test/` directory in the Project pane.
    2.  Select "Run 'Tests in ...'".
    3.  View the test results in the "Run" panel.

-----