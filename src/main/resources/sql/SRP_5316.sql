-- select all invalid customers
select u.id, u.email, u.first_name, u.last_name, FROM_UNIXTIME(u.created/1000) as created from userservice.users u where u.brand_id = '4NjU0sOz4sufDT4yrzngB6' and u.deleted is null and type = 'CUSTOMER' and (trim(u.first_name) = '' or trim(u.last_name) = '' OR u.last_name is null OR u.first_name is null) limit 10000;

-- select all active/future entitlements
select e.id, e.user_id, FROM_UNIXTIME(valid_from/1000) as valid_from, FROM_UNIXTIME(valid_to/1000) as valid_to, e.product_id, e.product_tags from entitlementservice.entitlements e where e.deleted is null and e.brand_id ='4NjU0sOz4sufDT4yrzngB6' and UNIX_TIMESTAMP() <= e.valid_to/1000 limit 100000;

-- select all active entitlements
select e.id, e.user_id, FROM_UNIXTIME(valid_from/1000) as valid_from, FROM_UNIXTIME(valid_to/1000) as valid_to, e.product_id, e.product_tags from entitlementservice.entitlements e where e.deleted is null and e.brand_id ='4NjU0sOz4sufDT4yrzngB6' AND e.valid_from < e.valid_to and UNIX_TIMESTAMP() <= e.valid_to/1000 AND UNIX_TIMESTAMP() > e.valid_from/1000 limit 100000;

-- select all future entitlements
select e.id, e.user_id, FROM_UNIXTIME(valid_from/1000) as valid_from, FROM_UNIXTIME(valid_to/1000) as valid_to, e.product_id, e.product_tags from entitlementservice.entitlements e where e.deleted is null and e.brand_id ='4NjU0sOz4sufDT4yrzngB6' and e.valid_from < e.valid_to AND UNIX_TIMESTAMP() <= e.valid_from/1000 limit 100000;