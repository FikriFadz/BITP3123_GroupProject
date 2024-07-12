# BITP3123_GroupProject

---------------------------------------------------------------------------------------------

1. How many apps involved
- Landlord and Tenant Application

---------------------------------------------------------------------------------------------

2. Brief explanation each apps

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
