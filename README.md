# Excel Microservice
Microservice for Excel file manipulations using [Apache POI](https://poi.apache.org/).

### Features
- Use a JSON notation to inject string and numeric values into specified Excel cells and download the Excel file
- Upload Excel templates based upon which values can be injected

Below is the description of the API calls.
 

**1. Add Template**
----
  Upload an XLSX template and get the template ID for use in future calls.

* **URL**

  /template/add

* **Method**

  `POST`
  
*  **Data Params**

   **Required:**
 
   `uploaded_file=[multipart]`

* **Success Response:**

  * **Code:** 200 
    **Content:** `077f6afb-50b4-486f-9a6d-1deae276b629`


**2. Get Template**
----
  Download a previously uploaded template based on a template ID.

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
  Create an Excel file without using a template.
  The data parameter is a JSON describing what value should be set in which cells.
  The structure is as follows:
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
  {0: {1: {3: {'value':'Hello', 'type':'string'}, 4: {'value':'World', 'type':'string'}}}}
  ```
  
  This will in the first sheet inject the strings 'Hello' in cell D2 and 'World' in E2. 

* **URL**

  /excel/create

* **Method**

  `POST`
  
* **Body**
  data[string]
  
*  **Data Params**

   **Required:**
   `fileName=[string]`
   
 * **Header**
   Content-Type: application/json

* **Success Response:**

  * **Code:** 200 
    **Content:** `file`
 

**4. Excel Inject**
----
  Inject data into an Excel file according to a previously added template.
  The data parameter uses the same format as that of the Excel Create call.

* **URL**

  /excel/inject

* **Method**

  `POST`
  
* **Body**

  [string]
  
*  **Data Params**

   **Required:**

   `fileName=[string]`
 
   `templateId=[string]`


* **Success Response:**

  * **Code:** 200
    **Content:** `file`
 
