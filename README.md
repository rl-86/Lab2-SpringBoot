# Encore Leshan Demo

This project combines [Eclipse Leshan LwM2M Server](https://github.com/rikard-sics/leshan) (rikard-sics fork) with an Encore-based backend API.


## Prerequisites
* Download [leshan-edhoc-1.0.14.zip](https://github.com/rikard-sics/leshan/releases/tag/v1.0.14), for the "leshan-client-demo.jar"
* Make sure you have the following installed:
	* [Docker](https://www.docker.com/products/docker-desktop/)
	* [Node.js](https://nodejs.org/dist/v22.16.0/no) - required to run "npm" (download LTS version)

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
* Create a folder called "nginx" in the root and place the `.htpassword` inside.

**6. Build the Encore Docker image.** 
* Make sure that `infra-config.json` is located in the project root.
	````
	encore build docker --config \infra-config.json encore-leshan-demo:1.0.0
	````
 
**7. Start the stack**

	docker compose up


**8. Access Leshan Web UI**
* Open http://localhost/bs and http://localhost/dm in a new tab and log in.

**9. Test API endpoints** (e.g., using Postman or Insomnia)

* Create Bootstrap Endpoint (example: Test)
	````
	POST http://localhost/api/bsclients/Test
 
	Headers:
  	Content-Type: application/json
  	Authorization: "Token"
 
	Body: (from BS_config.txt)
 	````
* Create Security Configuration
	````
	PUT http://localhost/api/clients
 
	Headers:
  	Content-Type: application/json
  	Authorization: "Token"
 
	Body: (from security_config.txt)
	````
**10. Verify via Leshan Web UI**

* Confirm the created bootstrap and security configs appear in both the Bootstrap and Device-manager interfaces.


**11. Run Leshan Client**
* Locate the `leshan-client-demo.jar` file and run it with the following settings. You can download it here: [leshan-edhoc-1.0.14.zip](https://github.com/rikard-sics/leshan/releases/tag/v1.0.14)
	````
	java -jar ./leshan-client-demo.jar -b -n Test -msec AAAA -sid BB -rid CC -u 127.0.0.1
	````
 
**12. Confirm device registration**

* The client should now appear in the Leshan Server under the "Clients" tab.



