# Encore Leshan Demo

This project combines the [Eclipse Leshan LwM2M Server](https://github.com/rikard-sics/leshan) with an Encore-based backend API for secure device bootstrapping and configuration.

## 🛠 Setup Instructions

Follow these steps to get the project up and running locally:

1. **Clone the repository**
   ```bash
   git clone https://github.com/AntonisTerzo/encore-leshan-demo.git

Install Encore CLI

bash
Kopiera
Redigera
curl -sL https://encore.dev/install | bash
Install dependencies

bash
Kopiera
Redigera
npm install
Add configuration files

Create a .env file in the project root.

Place a .htpassword file in the nginx/ directory.

Build the Encore Docker image

bash
Kopiera
Redigera
encore build ./infra-config.json
Start the stack

bash
Kopiera
Redigera
docker compose up
Open interfaces

Access http://localhost/bs

Open http://localhost/dm in a new tab and log in.

Test API endpoints (e.g., using Postman)

Create Bootstrap Endpoint

yaml
Kopiera
Redigera
POST http://localhost/api/bsclients/Test
Headers:
  Content-Type: application/json
  Authorization: mySecretToken
Body: (from JSON file)
Create Security Configuration

yaml
Kopiera
Redigera
PUT http://localhost/api/clients
Headers:
  Content-Type: application/json
  Authorization: mySecretToken
Body: (from JSON file)
Verify via Leshan Web UI
Confirm the created bootstrap and security configs appear in both the BS and DM interfaces.

Run Leshan Client

bash
Kopiera
Redigera
java -jar ./leshan-client-demo.jar -b -n Test -msec AAAA -sid BB -rid CC -u 127.0.0.1
Confirm device registration
The client should now appear in the Leshan Server Demo under the "Clients" tab.
