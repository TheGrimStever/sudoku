This is a Kotlin Multiplatform project targeting Android, iOS, Desktop (JVM).

## Sudoku

My first attempt at setting up a Sudoku game in Kotlin Multiplatform. 

My initial goal for this first commit is to have a board displayed on Desktop, Android, and iOS with a hard-coded puzzle. I intend to swap in SwiftUI, but for now, I'm building this as a Compose app. Later, I will make composeApp into its own module in Gradle, but for this first run I took a shortcut.

I ended up with more classes than I initially thought I would need. I did know that I would need to keep track of what the given numbers are, and what the player entered numbers are. I had to change my conceptualize of what a `Board` is. 
Initially, I thought it was the whole Sudoku game board, but after putting down some code and digging into the internals, I decided that `Puzzle` should represent the Sudoku game board, and `Board` would be the internal representation, that way, I can have more than 1 board representation that the UI doesn't care about.

More classes were made to keep State manageable and decoupled from UI. That's why I split out `SudokuGame.kt` with `GameSnapshot.kt`. 
- `SudokuGame.kt` will keep the state
- `GameSnapshot.kt` will give a read-only view of the state.

`BoardValidator.kt` also got a boost as I decided to keep track of conflict history, in case player decides to ignore the first conflict and select another cell and place another digit, which could also be a duplicate.

#### UI
So far I've split out 3 components that will make up the display.
- `SudokuScreen.kt`
  - This is the entire screen: SudokuBoard + Numberpad
- `SudokuBoard.kt`
- `NumberPad.kt`

I will add more buttons to clear cells and switch to "Note Mode" (name of mode TBD), which will allow user to enter multiple digits as small numbers in the corner like placeholders.


---
* [/iosApp](./iosApp/iosApp) contains an iOS application. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

* [/shared](./shared/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    the [iosMain](./shared/src/iosMain/kotlin) folder would be the right place for such calls.
    Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./shared/src/jvmMain/kotlin)
    folder is the appropriate location.

### Running the apps

Use the run configurations provided by the run widget in your IDE's toolbar. You can also use these commands and options:

- Android app: `./gradlew :androidApp:assembleDebug`
- Desktop app:
  - Hot reload: `./gradlew :desktopApp:hotRun --auto`
  - Standard run: `./gradlew :desktopApp:run`
- iOS app: open the [/iosApp](./iosApp) directory in Xcode and run it from there.

### Running tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- Android tests: `./gradlew :shared:testAndroidHostTest`
- Desktop tests: `./gradlew :shared:jvmTest`
- iOS tests: `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…