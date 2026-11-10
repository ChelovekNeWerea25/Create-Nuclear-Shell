# Create: Nuclear Shell — NeoForge Edition

![Mushroom cloud](docs/screenshots/01-mushroom-cloud.png)

Nuclear artillery for **Create: Big Cannons** on **Minecraft 1.21.1 / NeoForge**.

One shell. One crater. Everything else is scenery.

---

## ☢️ What is it?

A high-impact expansion for Create: Big Cannons that adds a craftable **Nuclear Shell**: a
single-use artillery round that turns the impact zone into a glowing, irradiated crater and
leaves a mushroom cloud hanging over the battlefield.

This is the **NeoForge edition** of Create: Nuclear Shell. It replaces the archived Forge 1.20.1
version and — unlike it — does **not** depend on Alex's Caves anymore.

## 💥 Features

- 💣 **Nuclear artillery shell** with a massive blast radius (35 blocks by default)
- ☢️ **Radiation, poison and blindness** for everything caught in the blast
- 🌋 **Mushroom cloud** that unfolds over several seconds and is visible from far away
- 🔥 **Fire, knockback and screen shake** for nearby players
- 🕳️ **Progressive crater** — the ground is dug block by block, from the centre outwards
- 🧪 **Sequenced assembly recipe** for the shell (5 steps, 4 loops)
- ⌨️ **`/nuke [radius]`** command for testing (operator permission level 2)
- ⚙️ **Fully configurable** — blast radius, damage, radiation, crater speed, screen shake

## 📸 Screenshots

| | |
|---|---|
| ![Detonation](docs/screenshots/02-detonation.png) | ![Crater](docs/screenshots/03-crater.png) |
| The detonation, the crater is dug while the cloud rises | The finished crater, minutes later |
| ![Nuclear shell](docs/screenshots/05-nuclear-shell.png) | ![Crater close up](docs/screenshots/04-crater-closeup.png) |
| The nuclear shell | The crater up close |

## 🧪 How to craft it

The shell is made in a **Sequenced Assembler**:

```
Sturdy Sheet → Deploy TNT → Press → Deploy Precision Mechanism
             → Deploy Brass Sheet → Press
```

Loops the sequence 4 times for an 85% chance of a finished shell.

## ⚙️ Configuration

The config file is `config/canonnukes-common.toml`:

| Key                            | Default | Description                                |
|--------------------------------|---------|--------------------------------------------|
| `explosion.blastRadius`        | `35.0`  | Blast radius of the shell in blocks        |
| `explosion.damageMultiplier`   | `1.0`   | Multiplier for the damage to entities      |
| `explosion.destroyBlocks`      | `true`  | Whether the blast digs a crater            |
| `explosion.craterSteps`        | `40`    | Steps the crater is dug over               |
| `explosion.craterStepInterval` | `2`     | Ticks between two crater steps             |
| `effects.radiation`            | `true`  | Whether the blast irradiates entities      |
| `effects.radiationDuration`    | `800`   | Duration of the radiation (20 ticks = 1s)  |
| `effects.screenShake`          | `true`  | Whether the screen shakes                  |
| `effects.mushroomCloud`        | `true`  | Whether the mushroom cloud is rendered     |

## 📦 Requirements

- Minecraft **1.21.1**
- **NeoForge** 21.1+
- **Create** 6.0.10+
- **Create: Big Cannons** 5.11+
- **Ritchie's Projectile Library** 2.1+

## 🌍 Translations

English, German, Russian, Ukrainian and Chinese are included. More are welcome — copy
`assets/canonnukes/lang/en_us.json`, translate the values and open a pull request.

---

> *"Now I am become Death, the destroyer of worlds."* — J. Robert Oppenheimer
