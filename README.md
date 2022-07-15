flightcommand
=============
flightcommand adds the ability to provide creative flight in survival mode,
both as a gamerule to affect all players, and as a command to provide flight selectively to specific
users.

## Usage
The gamerule is 'doCreativeFlight':

    /gamerule doCreativeFlight true

The command for individual flight is 'flight':

To show status:

    /flight hextun

To change status:

    /flight hextun true

## Building
I use IntelliJ and the Windows command shell for gradlew/git commands for development. Prior to the
addition of the access transformers, I could safely have two commands ready to go:

    gradlew genIntellijRuns
    gradlew clean build

It's possible I've not got my environment quite right, but since then, I have to build in this order:

    gradlew clean
    gradlew genIntellijRuns
    gradlew build

<<<<<<< HEAD
That's only if I want the effect of a 'clean build'. For normal builds I can just run a typical 
=======
That's only if I want the effect of a 'clean build'. For normal builds I can just run a typical
>>>>>>> 1.16.5
'gradlew build'.

I can do that same sequence in the IDE, but again, I may have something misconfigured.

## Permissions
Feel free to use my mod in your modpack. Please let me know if you do, as it's nice to hear from folks.

## Fun Facts
### Why did you make this mod when there are so already others out there?
I'm a coder; I code things. I made this mod initially so my wife could fly in our LAN world. Because of
how my son introduced me to Minecraft to begin with, I felt compelled to go through the mod development
and release process.

### Why so many releases recently?
My code was buggier than it is now. Much so. I've fixed so much at this point that I'm not sure how it was
working in the first place. Honestly I hadn't even attempted to run it against a dedicated server,
just doing testing in single player instance, both with and without LAN. Ugh.

### What's up with the access transformers?
Ask me about Capabilities and dead Players.

### What's up with ca--
So, you'll see in the release history that at one point I released a fix for values not transferring after
you die.

I'm using Capabilities [[1.16]](https://docs.minecraftforge.net/en/1.16.x/datastorage/capabilities/)
[[1.17]](https://docs.minecraftforge.net/en/1.17.x/datastorage/capabilities/)
[[1.18]](https://docs.minecraftforge.net/en/1.18.x/datastorage/capabilities/)
[[1.19]](https://docs.minecraftforge.net/en/1.19.x/datastorage/capabilities/) to manage flight. Possibly
overkill, in that they technically allow another mod to use this capability to manage flight. Unlikely.
But I liked the serialization and attachment options and went with it. I'm rethinking things.

Anyway, the documents are pretty explicit about wanting you to invalidate your capabilities when they go out
of lifecycle. When a Player dies, their object invalidates any capabilities attached to it. However,
you are told via the docs that on Player Clone event, when they are being cloned due to death, you can
call reviveCaps() to revive your capabilities, then fetch them for copying to the new Player.

Except that deeper in, the capabilities are held by a dispatcher, who took the time to mark each LazyOptional
it had as invalid. Including yours in your old Player object. Unfortunately there was no option to easily
ask for an unfiltered view of the dispatcher's capabilities, so I resorted to access transformation
to be able to peek back in.