# BITP3123_GroupProject

---------------------------------------------------------------------------------------------

1. How many apps involved
- Landlord and Tenant Application

---------------------------------------------------------------------------------------------

2. Brief explanation each apps

- Landlord Application

   1.	List Property:
  - Purpose: Enables landlords to add new properties to the rental listing.
  - Process: Landlords fill out a form with property details (e.g., address, rent, description, images) which gets saved to the database.
    
   2.	View Listed Properties:
  - Purpose: Allows landlords to see all properties they have listed.
  - Process: Displays a dashboard with a list of properties and options to view more details of each property.

   3.	Update Property Details:
  - Purpose: Allows landlords to modify the details of their listed properties.
  - Process: Provides an edit form pre-filled with the existing property details, which can be updated and saved to the database.

   4.	Remove Property:
  - Purpose: Enables landlords to remove properties from the listing.
  - Process: Provides an option to delete properties from the list and removes them from the database.

   5.	Send Monthly Rental Details to Tenant:
  - Purpose: Automatically sends rental payment details to tenants each month.
  - Process: Generates rental details periodically (e.g., via a script or cron job) and sends them to tenants through email or notifications.


- Tenant Application

  1.	Search Properties:
  - Purpose: Allows tenants to find available rental properties.
  - Process: Tenants use a search form with filters (e.g., location, price range) to find properties, which are fetched from the database and displayed.
    
   2.	View Property Details:
  - Purpose: Provides detailed information about a selected property.
  - Process: Tenants can view a detailed page showing the property's description, images, rent, and other relevant information.
    
   3.	View Monthly Rental Detail:
  - Purpose: Lets tenants view their monthly rental payment details.
  - Process: Displays a dashboard with rental details fetched from the database for each tenant.
    
   4.	Pay Rent:
  - Purpose: Enables tenants to make rent payments online.
  - Process: Tenants fill out a payment form with their payment details, which are processed through an integrated payment gateway, and the database is updated with the payment information.
    
   5.	View Payment History:
  - Purpose: Allows tenants to see their past rent payments.
  - Process: Displays a dashboard with a history of payments, fetched from the database, showing dates, amounts, and statuses of past payments.

---------------------------------------------------------------------------------------------

3. Architecture/Layer diagram for each of the apps including the middleware

---------------------------------------------------------------------------------------------

4. List of URL end points middleware RESTful

- "http://localhost/dad_project/insert_property.php"
- "http://localhost/dad_project/insert_tenant.php"
- "http://localhost/dad_project/get_properties.php"
- "http://localhost/dad_project/get_tenant_details.php?property_id=" + propertyId
- "http://localhost/dad_project/get_property_details.php?id=" + propertyId
- "http://localhost/dad_project/insert_rent_details.php"
- "http://localhost/dad_project/get_tenants.php"
- "http://localhost/dad_project/delete_property.php"
- "http://localhost/dad_project/update_property.php"
- "http://localhost/dad_project/get_due_rent_months.php"
- "http://localhost/dad_project/get_user_property_details.php"
- "http://localhost/dad_project/get_monthly_rental_details.php"
- "http://localhost/dad_project/get_rent_details.php"
- "http://localhost/dad_project/update_rent_status.php"
- "http://localhost/dad_project/get_paid_rent.php"
- "http://localhost/dad_project/get_due_rent_months.php"
- "http://localhost/dad_project/login.php"

---------------------------------------------------------------------------------------------

5. Functions/Features in the middleware

- Check Connection
- Delete Property
- Get Due Rent Months
- Get Monthly Rental Details
- Get Paid Rent
- Get Properties
- Get Property Details
- Get Rent Details
- Get Tenant Details
- Get Tenant
- Get User Property Details
- Insert Property
- Insert Rent Details
- Insert Tenant
- Login
- Update Property
- Update Rent Status

---------------------------------------------------------------------------------------------

6. The database and tables involve in the projects

Database Name: rental_app

Tables: 
1. properties
   - id, name, address, amount, tenant_name
     
2. rent
   - id, property_id, email, rent_amount, utility_amount, total_rent, month_year, status, payment_date
     
3. tenants
   - id, name, email, password, phone, property_id
