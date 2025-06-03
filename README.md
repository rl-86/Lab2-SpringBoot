# Encore Leshan Demo

This project combines [Eclipse Leshan LwM2M Server](https://github.com/rikard-sics/leshan) (rikard-sics fork) with an Encore-based backend API.


## Prerequisites
* Download [leshan-edhoc-1.0.14.zip](https://github.com/rikard-sics/leshan/releases/tag/v1.0.14), for the "leshan-client-demo.jar"
* Make sure you have the following installed: [Node.js](https://nodejs.org/dist/v22.16.0/no) - required to run "npm" (download LTS version)

You can check if Node.js is installed by running:

	node -v

## ⚙️ Setup Instructions

Follow these steps to get the project up and running locally:

**1. Clone the repository**
   
	git clone https://github.com/AntonisTerzo/encore-leshan-demo.git


**3. Install Encore CLI for Windows** (for other platforms, visit: https://encore.dev/docs/ts/install )

	iwr https://encore.dev/install.ps1 | iex


**4. Install dependencies**

	npm install


**5. Add configuration files**

* Place the `.env` file in the project root.
* Place the `.htpassword` file in the nginx/ directory.


**6. Build the Encore Docker image, using PowerShell.** 
* Make sure to provide your exact path to `infra-config.json`, located in the project root:
* (e.g. "C:/user/encore-leshan-demo/infra-config.json")
	````
	encore build --config ./infra-config.json
	````
 
**7. Start the stack**
	````
	docker compose up
	````

**8. Access Leshan Web UI**
* Open http://localhost/bs

* Open http://localhost/dm in a new tab and log in.


**9. Test API endpoints** (e.g., using Postman)

* Create Bootstrap Endpoint (example: Test)
	````
	POST http://localhost/api/bsclients/Test
 
	Headers:
  	Content-Type: application/json
  	Authorization: "Token"
 
	Body: (from BS_config.txt)

* Create Security Configuration
	````
	PUT http://localhost/api/clients
 
	Headers:
  	Content-Type: application/json
  	Authorization: "Token"
 
	Body: (from security_config.txt)

**10. Verify via Leshan Web UI**

* Confirm the created bootstrap and security configs appear in both the Bootstrap and Device-manager interfaces.


**11. Run Leshan Client**

	java -jar ./leshan-client-demo.jar -b -n Test -msec AAAA -sid BB -rid CC -u 127.0.0.1


**12. Confirm device registration**
    
	The client should now appear in the Leshan Server under the "Clients" tab.



