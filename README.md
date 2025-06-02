# Encore Leshan Demo

This project combines [Eclipse Leshan LwM2M Server](https://github.com/rikard-sics/leshan) with an Encore-based backend API.


## ⚙️ Setup Instructions

Follow these steps to get the project up and running locally:

1. **Clone the repository**
	```bash
	git clone https://github.com/AntonisTerzo/encore-leshan-demo.git
	````

2. Install Encore CLI for Windows (for other platforms, visit: https://encore.dev/docs/ts/install )

	```bash
	iwr https://encore.dev/install.ps1 | iex
	````

3. Install dependencies
	````bash
	npm install
	````

4. Add configuration files

	* Place the .env file in the project root.
	* Place the .htpassword file in the nginx/ directory.


5. Build the Encore Docker image. Make sure to provide the correct path to `infra-config.json`, located in the project root:

	```bash
	encore build ./infra-config.json
	````


6. Start the stack

	````bash
	docker compose up
	````



7. Access Leshan Web UI
	* Open http://localhost/bs

	* Open http://localhost/dm in a new tab and log in.


8. Test API endpoints (e.g., using Postman)

	* Create Bootstrap Endpoint (example: Test)
	````bash
	POST http://localhost/api/bsclients/Test
	Headers:
  	Content-Type: application/json
  	Authorization: "Token"
	Body: (from BS_config.txt)
	````

	* Create Security Configuration

	````bash
	PUT http://localhost/api/clients
	Headers:
  	Content-Type: application/json
  	Authorization: "Token"
	Body: (from security_config.txt)

	````

9. Verify via Leshan Web UI

	Confirm the created bootstrap and security configs appear in both the BS and DM interfaces.


10. Run Leshan Client

	````bash
	java -jar ./leshan-client-demo.jar -b -n Test -msec AAAA -sid BB -rid CC -u 127.0.0.1
	````

11. Confirm device registration
    
	The client should now appear in the Leshan Server under the "Clients" tab.



