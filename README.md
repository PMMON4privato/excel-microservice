# Excel Microservice
Microservice for Excel file manipulations using Apache POI and running on Spark Framework
Below is summary of all possible API calls.

**1. Add Template**
----
  Upload a XLSX template and get the template ID to use for future calls.

* **URL**

  /template/add

* **Method**

  `POST`
  
*  **Data Params**

   **Required:**
 
   `uploaded_file=[multipart]`

* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `077f6afb-50b4-486f-9a6d-1deae276b629`


**2. Get Template**
----
  Download a template based on template ID

* **URL**

  /template/get

* **Method:**

  `GET`
  
*  **URL Params**

   **Required:**
 
   `templateId=[string]`

   `fileName=[string]`


* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `file`


**3. Excel Create**
----
  Create an Excel file using no template.
  The data parameter is a JSON describing wich cells should be wich value.
  The structure is as such:
  ```
  {sheetIndex: {rowIndex: {cellIndex: {'value':'some value', 'type':'string'}}}}
  ```
  
  The types can be:
  ```
  string
  number
  ```
  
  Example:
  ```
  {0: {1: {3: {'value':'Hello World', 'type':'string'}, 4: {'value':'World'}}}}
  ```
  
  This will inject in the first sheet the strings 'Hello'in cell D2 and 'World' in E2. 

* **URL**

  /excel/create

* **Method**

  `POST`
  
*  **Data Params**

   **Required:**

   `data=[string]`
 
   `fileName=[string]`


* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `file`
 

**4. Excel Inject**
----
  Inject data into an Excel file using a pre-added template.
  The data parameter is the same for Excel Create.

* **URL**

  /excel/inject

* **Method**

  `POST`
  
*  **Data Params**

   **Required:**

   `data=[string]`
 
   `fileName=[string]`
  
    `templateId=[string]`


* **Success Response:**

  * **Code:** 200 <br />
    **Content:** `file`
 
