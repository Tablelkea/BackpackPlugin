# 🎒 BackpackPlugin

> Plugin Minecraft **Paper 1.21.1** ajoutant un système complet de sacs à dos craftables, upgradables via une forge et des runes.

---

## ✨ Fonctionnalités

### 🎒 Sacs à dos
- **3 niveaux** de sacs à dos avec des capacités croissantes
    - Niveau 1 → 36 slots
    - Niveau 2 → 45 slots
    - Niveau 3 → 54 slots
- Chaque sac est **unique** (identifiant UUID) et **persistant** entre les sessions
- Accès au sac en **clic droit** depuis n'importe où dans le monde
- La dernière ligne de l'inventaire est réservée aux **raccourcis** débloqués via les runes

### ⚒️ Forge de Sac
- Bloc craftable permettant d'**appliquer des runes** sur les sacs
- Interface GUI dédiée avec slot sac, slot rune et bouton de confirmation
- Les items sont **restitués** automatiquement à la fermeture du GUI

### 🔮 Runes
| Rune | Matériau | Effet |
|------|----------|-------|
| **Rune de Craft** | Nautilus Shell | Ajoute une table de craft accessible depuis le sac |
| **Rune d'Ender** | Ender Eye | Ajoute un accès à l'EnderChest depuis le sac |
| **Rune d'Âme** | Totem of Undying | Le sac est conservé à la mort (keepInventory local) |

---

## 🛠️ Crafts

### Sacs à dos
```
A B A        A B A        A B A
D C D        D C D        D C D
A B A        A B A        A B A
```
| | Niveau 1 | Niveau 2 | Niveau 3 |
|-|----------|----------|----------|
| **A** | Leather | Iron Ingot | Gold Ingot |
| **B** | String | Blaze Rod | Ender Eye |
| **C** | Chest | Player Head (N-1) | Player Head (N-1) |
| **D** | Iron Ingot | Gold Ingot | Diamond |

> ⚠️ Les niveaux 2 et 3 nécessitent le sac du niveau précédent au centre.

### Forge de Sac
```
A B A
B C B
A B A
```
| Lettre | Item |
|--------|------|
| **A** | Iron Ingot |
| **B** | Blaze Rod |
| **C** | Smithing Table |

### Runes
Toutes les runes suivent le même patron :
```
A B A
B C B
A B A
```
| Rune | A | B | C |
|------|---|---|---|
| **Rune de Craft** | Iron Ingot | Blaze Rod | Crafting Table |
| **Rune d'Ender** | Gold Ingot | Ender Eye | Ender Chest |
| **Rune d'Âme** | Gold Ingot | Totem of Undying | Nether Star |

---

## 📋 Commandes

| Commande | Description |
|----------|-------------|
| `/debug` | Active/désactive le mode debug (donne tous les items du plugin) |

---

## ⚙️ Compatibilité

| | |
|-|-|
| **Serveur** | [Paper](https://papermc.io/) |
| **Version Minecraft** | 1.21.1 |
| **Java** | 21+ |

> ⚠️ Ce plugin est **exclusivement compatible avec Paper**. Il ne fonctionnera pas sur Spigot ou Bukkit vanilla.

---

## 📦 Installation

1. Téléchargez le `.jar` depuis les [Releases](https://github.com/Tablelkea/BackpackPlugin/releases)
2. Placez-le dans le dossier `plugins/` de votre serveur Paper
3. Redémarrez le serveur
4. Les recettes sont automatiquement découvertes à la connexion de chaque joueur

---

## 🔧 Compilation

```bash
git clone https://github.com/Tablelkea/BackpackPlugin.git
cd BackpackPlugin
mvn clean package
```

Le `.jar` compilé se trouve dans `target/`.

---

## 📄 Licence

Ce projet est open-source. Toute contribution est la bienvenue via une Pull Request.