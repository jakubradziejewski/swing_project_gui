# 🥕 Carrot Farm Simulation

[![Java](https://img.shields.io/badge/Java-8%2B-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Swing](https://img.shields.io/badge/Swing-GUI-437291?style=flat-square&logo=java&logoColor=white)](https://docs.oracle.com/javase/tutorial/uiswing/)
[![Multithreading](https://img.shields.io/badge/Multithreading-Enabled-green?style=flat-square)](https://docs.oracle.com/javase/tutorial/essential/concurrency/)

## 💡 Overview

Multi-threaded Java Swing application simulating a carrot farm where farmers plant crops, rabbits try to eat them, and dogs chase the rabbits away. Each entity runs independently on its own thread.

**University Project:** Created as coursework for Object-Oriented Programming classes.

---

## 🎮 Game Entities

### 👨‍🌾 Farmers
Plant seeds in empty cells, repair damaged crops, and alert their companion dog when they spot rabbits nearby. Each farmer automatically gets a dog companion when spawned.

**Threading:** Runs on separate thread, performs atomic cell operations to avoid conflicts when modifying grid state.

**Adjustable:** Plant time (500-10000ms), repair time (500-10000ms)

### 🐰 Rabbits
Spawn randomly, wander around eating carrots until they've had their fill (3-7 carrots) or get caught by a dog.

**Threading:** Each rabbit gets its own thread spawned dynamically during gameplay. Uses `CopyOnWriteArrayList` to handle adding new rabbits while others are being iterated. The `volatile isActive` flag lets dogs signal when they've caught a rabbit.

**Adjustable:** Spawn rate (0.0-1.0 probability per growth cycle)

### 🐕 Dogs
Follow their farmer around until alerted about a rabbit, then chase it down. Return to wandering once the rabbit is caught.

**Threading:** Separate thread from their farmer. Shares target rabbit references with the farmer thread, so needs to check if rabbits are still active before interacting.

**Adjustable:** Detection range (1-10 cells)

### 🥕 Carrots
Grow through 5 stages before they're ready. Rabbits can damage them, farmers can repair the damage.

**Threading:** A dedicated growth thread updates all cells periodically while individual entity threads modify specific cells. Cell operations are atomic to prevent corruption during concurrent access.

**Adjustable:** Growth time between cycles (100-5000ms)

---

## 🔒 Thread Safety

The simulation handles concurrent access using:
- **`CopyOnWriteArrayList<Entity>`** for safe iteration while adding/removing entities
- **`volatile` flags** for cross-thread visibility of active states
- **Atomic cell operations** to prevent partial state updates
- **Proper shutdown** with `interrupt()` and `join()` to clean up threads

---

## 🖥️ GUI

Built with Java Swing using custom rendering with `Graphics2D`. Refreshes at 100ms intervals (10 FPS) with antialiasing enabled.

---

## 🚀 Quick Start

```bash
# Compile
javac *.java

# Run
java GameGUI
```

Initial dialog lets you set field size (5-20), number of farmers (1-5), and game parameters.

---

## ⚙️ Configuration

Runtime adjustments via config dialog:

| Parameter | Range | Description |
|-----------|-------|-------------|
| Carrot Growth Time | 100-5000ms | How fast carrots grow |
| Farmer Repair Time | 500-10000ms | Time to fix damaged cells |
| Farmer Plant Time | 500-10000ms | Time to plant seeds |
| Rabbit Spawn Rate | 0.0-1.0 | Spawn probability each cycle |
| Dog Detection Range | 1-10 cells | How far dogs can detect rabbits |

---

## 🎨 Custom Graphics

Optional: Add images to `img/` directory (`rabbit.jpg`, `farmer.jpg`, `farmer1.jpg`, `dog.jpg`, `carrot.png`). Falls back to colored shapes with letters if images aren't found.

---

## 💾 Save/Load

Saves grid cell states, growth stages, and config parameters using Java serialization. Entity positions and threads aren't saved - farmers respawn randomly when you load, rabbits and dogs are cleared.
