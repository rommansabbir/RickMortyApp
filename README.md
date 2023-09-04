# Rick & Morty

## Build and Run
- Clone the Repository.
- Build and Run the Project.

## Testing
- Run the command `gradle app:testDebugUnitTest` (Debug) or `gradle app:testReleaseUnitTest` (Release).

---

## Project Detail
**Note**: This is a demo project, and you may notice that there is a significant amount of code that may not be necessary for a project of this scale. My primary goal was to establish a robust project architecture that can easily be scaled and, more importantly, maintainable and testable.

- Clean Architecture
- Dependency Injection
- MVVM
- JetPack Compose
- REST
- Local caching
- Testing

---

## Project Architecture
![Project Architecture](https://github.com/rommansabbir/RickMortyApp/assets/25950083/3196183b-1d10-45a9-ba81-78bb76314b2e)
### UI ↔️ Domain ↔️ Data

Let's explore...

**Data Layer**:
- The data layer is responsible for providing data to the client (UI).
- It operates independently of the rest of the application.
- The data layer primarily contains the Repository, API Service, Local Caching, Local Database, and their respective implementations.
- It should be designed to be independently testable.
- While the data layer should consist mainly of pure Java/Kotlin code, in some cases, it may include framework dependencies (e.g., **Local Caching** requiring `Context`).
- Clients request data from the data layer to obtain the required information.

**Domain Layer**:
- The domain layer acts as an **Interactor** between various components, modules, or systems.
- It primarily houses **UseCases**.
- Clients request actions from the **Domain Layer** (UseCase), which then delegates these requests to the **Data Layer** and returns the response to the client.
- Each business logic component has its dedicated **UseCase** (e.g., the Login functionality should have its dedicated **UseCase**).
- The UseCase ensures that ongoing requests **DO NOT RUN ON THE UI THREAD** but rather on separate Threads/Scopes.
- Each UseCase should have only one `API`.

**UI Layer (Client)**:
- This layer is responsible for presenting data in the user interface.
- It's divided into two components: `View` and `ViewModel`. `View` is responsible for displaying the UI, while `ViewModel` handles the business logic of that View and manages States.
- Clients initiate requests to specific **UseCases** (Domain Layer) to obtain data.

**In simple terms**:
- The UI initiates a request to the **UseCase**, which passes the request to the **Repository**.
- The **Repository** performs its operations and returns the data to the **UseCase**, which then passes it back to the **UI**.
- Additionally, the **UseCase** ensures that the entire operation is performed on a different _thread/scope_, not the _UI/Main_ thread.

---

## Testing
- APIs
- UseCases
- ViewModel

---
## Local Caching Strategy
When you open the app, it first attempts to retrieve data from the **local cache**. Here's how the caching strategy works:

- If the local cache contains data and the retrieval is successful, the app loads the character list from the local cache. Additionally, it retrieves the next paginated URL for potential future data requests from the remote source.
- If, for any reason, the app fails to retrieve data from the cache, it initiates a request to load data from the remote source.
- With each subsequent character list `API` call, the app updates the cache with the latest data and the URL for the next paginated results. This ensures that the app consistently maintains up-to-date information for a seamless user experience.

---

## Notes
- **AppResult** is a sealed class designed to represent the outcome of an execution, with two possible states: `Success` or `Error`.
- **Failure** is a global sealed class that covers all managed errors in the application, including HTTP errors, caching errors, and feature-specific errors, among others.
- **StringBuilderExtensions** contains extension functions for writing strings to variables. It's widely used in the **Data Layer** to map JSON to POJO/Model objects.
- **executeBodyOrReturnNull** is an extension function used to execute a given `body` or return `null` by utilizing Try/Catch under the hood.
- **ComponentActivity.mainScope** is an extension function for launching a new `Main Coroutine` using the associated `lifecycleScope`. This function is primarily used to perform operations within the `Activity`'s `lifecycleScope`.
- The state of **Views (Composables)** is managed within the `ViewModel`. Each **View** has its own **UI State**.
- **Network Availability** is managed within the **UI Layer** due to its dependency on the underlying framework.
- To check internet connectivity status, the **NetworkX** library is utilized, which was also developed by me.
- For local caching, **StoreX** is employed, another library developed by me. Instead of using **Room** or any other database for local caching, **StoreX** is preferred due to its superior computational performance. It stores data in the application's **Cache** folder.

---

## Missing Implementation
- [ ] Integration of `LiveData` (LiveData is currently not utilized).
---

## Dependencies
- Android Core (KTX)
- Android Lifecycle
- JetPack Compose
- Retrofit
- OkHttp
- Gson
- Hilt
- Glide Image (Compose)
- StoreX
- NetworkX
- JUnit
- Roboletric
- Android Test Core (KTX)
---
