# SwordCat

Android application built for a technical assessment by Sword Health. It allows users to browse cat breeds, view detailed information, and manage a list of favorite cats.

## Architecture

The project follows **Clean Architecture** principles and is structured into a multi-module system to ensure separation of concerns, scalability, and testability.

### Module Structure

- **`:domain` (Pure Kotlin)**: Contains the business data models (`Cat`), repository interfaces and other business logic. It has zero dependencies on the Android framework, making it extremely easy to test.
- **`:database` (Android Library)**: Persistence layer using **Room**. Handles local caching and user-specific states like favorites.
- **`:backend` (Android Library)**: Networking layer using **Retrofit** and **OkHttp**. Handles communication with the Cat API
- **`:app` (Android App)**: The UI layer. Built entirely with **Jetpack Compose**. It orchestrates the other modules and manages navigation.

> **Note**: `app` is intentionally the only module that contains Hilt, as it is the final glue and orchestrator of the dependency tree. I tend to avoid Hilt in child modules because it encourages self-configuring entities, which violates dependency inversion

### Testing

The project follows the **Testing Pyramid** strategy to ensure high reliability and showcase my testing habits:

- **Unit Tests**: The bulk of the testing suite. These cover business logic in ViewModels, data mapping in the database layer, and repository orchestration.
- **Integration Tests**: Used to verify the networking layer using **MockWebServer** to simulate real API responses and the database layer using **In-memory Room** databases.
- **UI Tests**: Screens are tested in isolation using the **Page Object Pattern**. This allows for declarative assertions and actions, making the tests readable and resilient to UI structure changes.

## Tech Stack

- **UI**: Jetpack Compose with Material 3 and Navigation 3 for navigation
- **Dependency Injection**: Hilt (Dagger).
- **Concurrency**: Kotlin Coroutines & Flow.
- **Networking**: Retrofit + OkHttp + KotlinX Serialization.
- **Database**: Room.
- **Image Loading**: Coil
- **Testing**: JUnit 4, Truth, MockWebServer, and Turbine for Flow testing.

## Key Decisions & Trade-offs

### 1. Offline-First Strategy
The app implements an **Offline-First** approach in the `CatRepository`. 
- **Decision**: When the user opens the list of cats, the app immediately displays cached data from the database before triggering a network refresh.
- **Trade-off**: This increases the complexity of the repository (managing multiple `Flow` emissions), but results in a significantly better UX, as the app feels instantaneous and works seamlessly without an internet connection.

### 2. Multi-Modules by layer
- **Decision**: The project was split into 4 distinct modules by layer
- **Trade-off**: While this adds some initial overhead in Gradle configuration, it enforces strict boundaries. For example, the `:domain` layer cannot accidentally access UI components, and incremental compilation is much faster. For larger-scale projects, I would also split the UI into dedicated feature modules (e.g., `:features:list`, `:features:detail`) to maximize isolation and build parallelism. In general, I tend to favor a module creation by reasonable responsibility

### 3. Favorite Logic Placement
- **Decision**: Favorite management is handled via a dedicated `CatFavoriteLocalSource` consumed directly by ViewModels, rather than being part of the `CatRepository`.
- **Trade-off**: This keeps the `CatRepository` focused strictly on the orchestration of data (Network + DB) and makes it transparent that favorites are a local-only user state. It prevents the repository interface from growing into a "God Object."
Since the Cat API only allows marking images as favorites (not cat ids), I decided for this approach to keep the code simple without unnecessary abstractions

### 4. Image Fetching
- **Decision**: Implemented a custom `Keyer` and `Fetcher` for Coil.
- **Trade-off**: Instead of passing raw URLs to Compose, the app passes a custom model called `ImageReference`. This was necessary because the cat api does not return image urls for each cat, but instead an image id. That same image id needs to be used in a separate API call to retrieve the image url.

### 5. Navigation events
- **Decision**: Implemented navigation events directly in the UI layer, near the click event
- **Trade-off**: In some cases, e.g tracking, we need to delegate these events to the ViewModel before navigating. For simplicity, I just invoked them directly because the requirements didn't mention this.

### 6. API Key Management
- **Decision**: The API key is hardcoded in the source code.
- **Trade-off**: This was done to ensure the app is immediately runnable by reviewers without requiring them to sign up for a Cat API key and configure it manually.
- **Security**: For production applications, the most secure approach is to never commit secrets to version control. Instead, keys should be injected at build time using environment variables or Gradle properties (e.g., via `BuildConfig`).
