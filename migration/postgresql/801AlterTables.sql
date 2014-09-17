-- Jun 16, 2014 3:19:27 PM VET
-- LVE ADempiere
ALTER TABLE AD_Field ADD COLUMN IsAllowsCopy CHAR(1) DEFAULT 'Y' CHECK (IsAllowsCopy IN ('Y','N'))
;
