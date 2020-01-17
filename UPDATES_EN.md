# TrMenu Update Logs #

#### VERSION 1.1
  - ##### 1.12
    - Since: 2020.1.14
    - Updates:
      - Improved ActionCommand(Player/OP/Console)
      - Added ActionGiveMoney
      - Added ActionTakeMoney 
      - Added ActionTakeItem (Remove specify items)
      - Now you can create internal menus in settings.yml
      - Added Traditional Chinese locale file (zh_TW)
      - R2
        - Added MenuOptions.Update-Inventory
        - Fixed animation buggy when there are multiple viewers
        - Added TrUtils, can be used in JavaScript
        - Recoded "Mat"
        - Added <dye: r,g,b> for create dyed leather armor
        - Added <banner: > for create dyed custom banner
      - R3
        - Added ChatCatcher, for example
          ```
          - |-
            Catcher:
              <Type=CHAT>
              <Before=Tell: &3&lPlease type a value>
              <Valid=TELL:&6You typed a number &a$input>
              <Invalid=TELL:&cInvalid number input>
              <Require=TrUtils.isNumber("$input")>
              <Cancel=TELL:&7Canceld...>
          ```
      - R4
        - Fixed menu file listener
        - Added notify message when auto-reload menu failed
        - Each icon's priority can be defined with "priority: [Num]" now
        - Fixed a bStats error
        - Improved item's ID reading
        - Now supports materials of Mods in Forge server
      - R5
        - Support .json file menu load
        - Fixed animate slots buggy when there're multiple viewers
        - Added variable $openBy which returns CONSOLE or PLAYER
  - ##### 1.11
    - Since: 2020.1.12
    - Updates:
      - R5
        - Improved "about" GUI
        - Add TrMenu PlaceholderAPI variables