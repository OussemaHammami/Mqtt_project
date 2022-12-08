import time
import paho.mqtt.client as mqtt
from faker import Faker


MQTT_BROKER_URL = "mqtt.eclipseprojects.io"
MQTT_PUBLISH_TOPIC = "measurement/co2"

mqttClient = mqtt.Client()
fake = Faker()

# connect to the MQTT broker
mqttClient.connect(MQTT_BROKER_URL, 1883)

# Infinite loop of fake data sent to the broker
while True:
    co2 = fake.random_int(min=200, max=400)
    mqttClient.publish(MQTT_PUBLISH_TOPIC, co2)
    print(f"Published new co2 measurement: {co2} to topic {MQTT_PUBLISH_TOPIC}")
    time.sleep(1)
