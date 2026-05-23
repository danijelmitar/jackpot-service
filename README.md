# Jackpot Service

A backend service that manages jackpot contributions and rewards. The service receives bets, contributes them to matching jackpot pools, and evaluates bets for jackpot rewards.

## How to Run

### Prerequisites
- Java 25
- Docker

### Start the service
```bash
./gradlew bootRun
```

Docker Compose will automatically start Kafka on startup via Spring Boot Docker Compose support.

### Run tests
```bash
./gradlew test
```

### H2 Console
Available at `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:file:./data/jackpot_db`
- Username: `sa`
- Password: *(empty)*

## API

### Place a bet
```
POST /api/v1/bets
Content-Type: application/json

{
  "userId": "user-1",
  "jackpotId": "jackpot-fixed",
  "amount": "10.00"
}
```

### Evaluate jackpot reward
```
POST /api/v1/jackpots/bets/{betId}/evaluate
```

Returns `200 OK` with reward amount (0 if didn't win), `404` if bet not yet evaluated.

## Seeded Jackpots

| ID | Strategy | Initial Pool | Details |
|---|---|---|---|
| `jackpot-fixed` | Fixed | 1000.00 | 10% fixed win chance per evaluation |
| `jackpot-variable` | Variable | 1000.00 | Chance grows from 0% to 100% as pool grows from initial to threshold (2000.00) |

## Architecture

### Modules
- **`bet`** — accepts bets, persists them, publishes `BetPlaced` event to Kafka
- **`jackpot`** — consumes `BetPlaced` events, contributes to jackpot pool, evaluates rewards

### Layer structure (each module)
```
api/            → REST controllers, DTOs
application/    → Use cases (@Transactional boundary)
domain/         → Aggregates, domain interfaces, business rules
infrastructure/ → JPA adapters, Kafka producer/consumer
```

### Communication
```
POST /api/v1/bets
    → PlaceBetUseCase → Kafka "jackpot-bets"
        → KafkaBetConsumer
            → ContributeToJackpotUseCase
```

## Event Storming

![image info](./docs/event_storming.jpg)

> Note: Event storming represents the domain analysis conducted before implementation.
> Not all commands and events are implemented as explicit classes — some are implicit
> in the use cases and domain methods.

### Domain Events
- `BetPlaced` — a bet was placed by a user on a jackpot
- `JackpotPoolIncreased` — the jackpot pool grew as a result of a contribution
- `JackpotRewardEvaluated` — a bet was checked whether it wins the jackpot
- `JackpotWon` — a bet won the jackpot reward
- `JackpotReset` — the jackpot pool returned to its initial value after being won

### Commands
| Command | Issued By | Triggers |
|---|---|---|
| `PlaceBet` | User | `BetPlaced` |
| `ContributeToJackpot` | System | `JackpotPoolIncreased` |
| `EvaluateJackpotReward` | User | `JackpotRewardEvaluated` → `JackpotWon` (conditionally) |
| `ResetJackpot` | System | `JackpotReset` |

### Policies
| When | Then |
|---|---|
| `BetPlaced` | → `ContributeToJackpot` |
| `JackpotWon` | → `ResetJackpot` |

## Technical Decisions

### Reward evaluation on endpoint call
Reward is evaluated when the client calls the evaluate endpoint, not automatically on bet placement. This is a known limitation — a player could wait for the pool to grow before evaluating. In production, evaluation should happen at contribution time inside the Kafka consumer.

### Concurrency
No locking mechanism is currently implemented. Concurrent evaluations could theoretically award the jackpot twice. In production this would be addressed with optimistic locking (`@Version`), pessimistic locking (`SELECT FOR UPDATE`), or an event-sourced approach.

### Strategy pattern
Contribution and reward strategies are hardcoded per strategy type but instantiated via factories (`ContributionStrategyFactory`, `RewardStrategyFactory`). Adding a new strategy requires:
1. New class implementing the strategy interface
2. New enum value in `StrategyType`
3. New case in the factory switch
4. New row in the `jackpot` table

### In-memory database
H2 is used as per assignment requirements. Schema is managed via `schema.sql` with `spring.jpa.hibernate.ddl-auto=none`.

### Kafka
Producer and consumer use a shared `BetPlacedMessage` contract in `common.messaging`. The `jackpot-bets` topic is created on startup via a `NewTopic` bean.
