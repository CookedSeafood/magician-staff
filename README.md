# Magician Staff

Magician Staff is an item that shoots explosive bats following the rotation of the shooter for 4 mana when right-clicked.

## Feature

- Rotate the head to rotate the bat.
- Explode on collision, not on death.

## The Item

An item where minecraft:custom_data.id is "magician_staff:magician_staff" is considered as an magician staff.

An example command that gives an magician staff to yourslef:

```mcfunction
/give @s netherite_sword[custom_data={id:"magician_staff:magician_staff"}]
```

Another example command for the original design:

```mcfunction
/give @s netherite_sword[custom_data={id:"magician_staff:magician_staff",modifiers:[{attribute:"pentamana:mana_capacity",base:6.0d,operation:"add_value",slot:"mainhand"}]},item_name={text:"Magician Staff"},item_model="netherite_shovel",rarity="rare"]
```

## Configuration

Below is a template config file `config/magician-staff.json` filled with default values. You may only need to write the lines you would like to modify.

```json
{
    "manaConsumption": 4.0,
    "movementSpeed": 1.0,
    "explosionFuse": 120,
    "explosionRadius": 1
}
```

### `manaConsumption`

Mana consumption per use.
