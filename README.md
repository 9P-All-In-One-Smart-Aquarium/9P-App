Final Version of Application to manage an aquarium

📱 Smart Aquarium App Development – Structure & Role Overview
In this project, I was responsible for developing the Android application (9P-App) used to manage and interact with the smart aquarium.

The app allows users to monitor the aquarium in real time, control actuators such as the water pump and heater, and receive immediate alerts when abnormal conditions occur.

Within the overall system, the app serves as the main interface connecting the user to the IoT platform (Mobius).

To make the workflow easy to understand, the app can be described in four intuitive stages.

1️⃣ Input Stage — Reading Aquarium Status
The app retrieves sensor data from the Mobius (oneM2M) platform, including:

Water temperature
Water level
Ambient light
Pump status
These values are uploaded by the hardware system (ESP32 / Raspberry Pi) and the app fetches the latest data periodically or upon user request.

In this stage, the app acts as a window that displays the aquarium’s current condition.

2️⃣ Data Processing Stage — Communicating with Mobius
The app communicates with Mobius using REST API calls, performing two key operations:

■ Fetching Sensor Data (GET)
Requests the latest ContentInstance (cin) from Mobius
Parses the JSON response using Gson
Updates the UI with real-time values
■ Sending Control Commands (POST)
When the user presses a control button (e.g., Pump ON), the app sends a command like:

{ "m2m:cin": { "con": { "Pump": "on" } } }
The Raspberry Pi, subscribed to this container, receives the command and operates the physical device accordingly.

At this stage, the app becomes the bridge that converts user actions into actual device operations.

3️⃣ Function Processing Stage — User-Centered Features

<img width="1280" height="671" alt="image" src="https://github.com/user-attachments/assets/d2f64015-9550-49cf-9158-c1de1ad79c5b" />

The app provides several key screens:

Dashboard: Real-time sensor monitoring
Device Control: Remote actuator control
Notification Log: Viewing received alert messages
Each screen is connected to Mobius through the Retrofit2 network layer.

ViewBinding is used for stable and clean UI rendering.

Especially, the Notification Log uses Firebase Cloud Messaging (FCM) to deliver immediate alerts whenever the system detects abnormal conditions.

4️⃣ User Interface Stage — Connecting Monitoring to Control
The app is designed to enhance user experience by enabling:

Real-time monitoring from anywhere
Instant control of devices such as pumps
Immediate notification through FCM when something unusual occurs
Therefore, the 9P-App does more than simple monitoring—it functions as a mobile dashboard that allows the user to operate the entire IoT system conveniently
