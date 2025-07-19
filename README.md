# Magician Staff

A **magician staff** is a casting weapon that is used to shoot explosive bats. It grants 6 additional mana capacity.

| Statistics ||
| - | - |
| Damage | 15 (Explosion) |
| Knockback | 0 |
| Mana Consumption| 4 |
| Velocity | 1 |
| Rarity | Rare |

## Usage

### Melee Attack

Same as a netherite sword.

### Casting Attack

Pressing use while holding a magician staff in main hand shoots an explosive bat and consumes mana.

#### Mana Consumption with Ultilization

| Base Mana Consumption | Utilization I | Utilization II | Utilization III | Utilization IV | Utilization V |
| :-: | :-: | :-: | :-: | :-: | :-: |
| 4.0 | 3.6 | 3.2 | 2.8 | 2.4 | 2.0 |

#### Explosive Bat

An **explosive bat** is a bat that flies forward continuously at the speed of 1 block per tick with the rotation of the caster entity and explodes with power 1 on collision or in 6 seconds.

The bat will not explode on death.

The explosion from the bat does not destroy blocks.

## Data Controlled

An item where `minecraft:custom_data.id` is "magician_staff:magician_staff" is considered as an magician staff.

### Give Command

```mcfunction
/give @s minecraft:netherite_sword[custom_data={id:"magician_staff:magician_staff",modifiers:[{attribute:"pentamana:mana_capacity",base:6.0d,operation:"add_value",slot:"mainhand"}]},item_name={text:"Magician Staff"},item_model="minecraft:netherite_shovel",rarity="rare"]
```

### Loot Table Entry

```json
{
    "type": "minecraft:item",
    "functions": [
        {
            "function": "minecraft:set_components",
            "components": {
            "minecraft:custom_data": {
                "id": "magician_staff:magician_staff"
            },
            "minecraft:item_model": "minecraft:netherite_shovel",
            "modifiers": [
                {
                    "attribute":"pentamana:mana_capacity",
                    "base":6.0,
                    "operation":"add_value",
                    "slot":"mainhand"
                }
            ],
            "minecraft:rarity": "rare"
            }
        },
        {
            "function": "minecraft:set_name",
            "name": {
                "text": "Magician Staff"
            },
            "target": "item_name"
        }
    ],
    "name": "minecraft:netherite_sword"
}
```

## Configuration

Below is a template config file `config/magician-staff.json` filled with default values. You may only need to write the lines you would like to modify.

```json
{
    "manaConsumption": 4.0,
    "movementSpeed": 1.0,
    "explosionFuse": 120,
    "explosionRadius": 1,
    "isParticleVisible": true
}
```
