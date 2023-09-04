# Rick & Morty

### Build and Run
1. Clone the Repository.
2. **Build** and **Run** the Project.

### Testing
3. Run the command `gradle app:testDebugUnitTest` (Debug) or `gradle app:testDebugUnitTest` (Release).
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
