SELECT e.transportOwner.name AS transportOwner,
e.payDate AS payDate,
e.value AS value,
e.journey.journeyNumber AS journey
FROM erp$MutualSettlements e