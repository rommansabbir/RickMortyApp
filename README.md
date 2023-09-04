# Rick & Morty

### Build and Run
1. Clone the Repository.
2. **Build** and **Run** the Project.

### Testing
3. Run the command `gradle app:testDebugUnitTest` (Debug) or `gradle app:testReleaseUnitTest` (Release).
---
### Project Detail
Note: This is a demo project but you might find out there are lots of codes which is not required for this king of test project. What I tried to acheive is having a proper project architecture to extend the project into a large scale project and most importantly makes it maintainable and testable.

- Clean Arch
- Dependency Injection
- MVVM
- JetPack Compose
- REST
- Local caching
- Testing

---

### Project Architecture
![image](https://github.com/rommansabbir/RickMortyApp/assets/25950083/3196183b-1d10-45a9-ba81-78bb76314b2e)
### UI ↔️ Domain ↔️ Data

Let's explore...

**Data Layer**:
- Data layer is resposnsible to provide data to the client (UI).
- Data layer is independent from the rest of the application.
- Data layer mainly contain Repository, API Serivce, Local Caching, Local Database including their respective implementation.
- Data layer should be testable independently.
- Data layer should contain pure java/kotlin code but in some certain cases may include Framework Dependency (`ex:` **Local Caching** require `Context`)
- Client request to data layer to provide the requested data.

**Domain Layer**
- Domain layer is more a like an **Interactor** between two component or module or system.
- Domain layer mainly contain **UseCase**'s.
- Client request to the **Domain layer** (UseCase) and **Domain layer** request to the **Data Layer** and then return the response to the client.
- Each business logic has their own **UseCase** (`ex:` Login functionality should have an **UseCase** of it's own).
- UseCase also make sure that the ongoing request __MUST NOT RUN UNDER THE UI THREAD__, rather than run on different Thread/Scope.
- UseCase must have only one `API`.

**UI Layer (Client)**
- Represent the data into UI.
- Divided into two parts (`View` and `ViewModel`). `View` present the `UI` and `ViewModel` to perform business logic of that View and manage **States**.
- Client make request to spcific **UseCase** (Domain Layer) to get the data.

Simply,
- UI request to the UseCase, UseCase pass the request to the Repository.
- Repository perform it's execution and return the data to th UseCase, UseCase then return the data to the UI.
- Also, UseCase make sure the the whole operation if performed under a differnent thread/scope but not in the UI/Main thread.
---

### Testing
- APIs
- UseCases
- ViewModel


---
### Notes
- **AppResult** is a sealed class to represent a execution result. It has two state `Success` or `Error` which some public `APIs`.
- **Failure** is a global sealed class that represent all managed errors in the application such as _HTTP Error_, _Caching Error_, _Feature Specfific Error_ etc.
- **StringBuilderExtensions** contain extension function to write string to a variable. Highly used in the **Data Layer** to map `JSON` to `POJO`/`Model`.
- **executeBodyOrReturnNull** is an extension function to execute a given `body` or return `null` by leveraging the `Try/Catch` under the hood.
- **ComponentActivity.mainScope** is an extension function to lauch new `Main Coroutine` by leveraging the associated `lifecycleScope`. The main use of this function to perform any execution under the `Activity`'s `lifecycleScope`.
- **View (Composable)** state is managed inside `ViewModel`. Each **View** has it's own **UI State**
- **Network Availability** is managed from the **UI Layer** because of having dependency on the **Framework**.
- To check internent connectivity status, used **NetworkX** library which is also developed by me.
- For local caching **StoreX** is used, which is also developed by me. I haven't use **Room** or any other _database_ for local caching since the computation performance is higer for Database and **StoreX** is file based caching library, store data in the application's **Cache** folder.
---
### Missing impl
- [ ] Usages of `LiveData` (`LiveData` is not used.)
