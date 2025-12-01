# Blackjack — Team Project

A Java-based Blackjack game structured using Clean Architecture.  
This project separates core game logic from UI and external dependencies, making the system modular, testable, and easy to extend.

## Overview

This application implements a full Blackjack game with standard rules and supports betting, dealing, hit/stand/double/split actions, dealer behavior, and payout calculation.  
The project follows Clean Architecture to ensure clear separation of concerns and maintainability.

## Features

- Full Blackjack gameplay flow
- Clean Architecture layering
- Entities for Cards, Hands, Players, Dealer, and GameState
- Use cases for dealing, player actions, ending rounds, and resetting the game
- Interface Adapters connecting use cases to UI/Frameworks
- Pluggable `DeckProvider` implementations (random, deterministic, etc.)
- Unit-testable business logic

## Project Structure

## Project Structure

The project follows Clean Architecture, separating the code into clear layers:  
**entities → usecase → interface_adapters → frameworks_and_drivers**.

### `entities/`
Contains the pure business objects and rules.  
These classes have no dependencies on UI or external systems.

- `Card`, `Deck`, `Hand`, `Player`, `Dealer` — Core domain models.
- `Bet`, `BetType`, `BetStatus` — Represent betting behavior.
- `Game`, `GameState` — Track the state and flow of a Blackjack round.
- `DeckProvider` — Abstraction for card-drawing logic (supports dependency inversion).

### `usecase/`
Implements the application-specific rules and game operations.  
Each subfolder corresponds to one gameplay action (e.g., dealing, hitting, starting, saving).

Each use case contains:
- **InputBoundary** — interface defining the request.
- **InputData** — request model.
- **Interactor** — use case logic.
- **OutputBoundary** — interface defining the response.
- **OutputData** — response model.

This layer orchestrates the core game flow without knowing anything about UI or storage.

### `interface_adapters/`
Bridges between the UI and the use cases.

Each subfolder contains:
- **Controllers** — convert user/UI actions into `InputData`.
- **Presenters** — format `OutputData` into view-friendly models.
- **ViewModels** — hold data to be displayed by the UI.

This layer ensures that the UI never directly talks to use cases or entities.

### `frameworks_and_drivers/`
Contains external and platform-specific code.

Includes:
- **Data Access Implementations** (e.g., loading/saving games)
- **Deck implementations** (e.g., random deck logic)
- **Start, update, exit modules**
- `Main.java` — entry point for launching the application

This layer depends on the inner layers, never the reverse.

### `resources/`
Static resources required by the application (if any).

### `test/`
JUnit test classes for ensuring correctness of the core logic.

Note: The reason why "HarryMaguire" has so many commits is because he made a lot of unnecessary commits when trying to reorganize the files. As a result, the number is heavily inflated.
