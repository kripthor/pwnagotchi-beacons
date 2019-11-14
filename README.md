# pwnagotchi-beacons
A pwnagotchi plugin that sends status messages encapsulated inside a valid beacon frame. They are easy to process from an Android/iOS application.

# What's the point?
This makes it possible to see a pwnagotchi status without e-ink/lcd screen and without the BLE pairing process, a process that not all users can establish. Since that the status is transmitted compacted inside a valid wifi beacon frame, it's fairly easy to produce applications (Android/iOS/others) that can parse this information without to much privileges.
It's also possible to monitor several pwnagotchi's at the same time, there could be different headless pwnagotchis running on different channels and being monitored on one central app.

# Installation
At the moment you will need to:
1. Install python3-scapy on the pwnagotchi (apt install python3-scapy)
2. Copy the plugin/beacons.py file to the current plugins directory (usually /usr/local/lib/python3.7/dist-packages/pwnagotchi/plugins/default)
3. Edit the config.yml file and add the new plugin. See config-example.yml file.

That's it. You can use the provided Android APK to test if everything is working (only 1 client support for now. Source will be provided soon, at the moment you'll need to manually install the apk, it sucks, I know).

# Pros and Cons
## Downsides
It is a slow protocol, the updates are not anywhere near realtime, but neither certain e-ink displays.
It is incomplete. Not all information visible on the displays are visible via pwnagotchi-beacons. Not yet anyway.

## Upsides
You don't need an LCD/e-ink display, just use your smartphone.
You can monitor several devices at the same time.
You don't need to worry about setting up BLE PAN or any other method.

# More info
This release is not even beta. Help developing is welcomed.
Feel free to reach me via @kripthor or in pwnagotchi Slack #plugins channel

# Kudos
Special thanks to Dipsylala for being so helpful and for his HTML UI that I ended up cannibalizing in the Android app.
