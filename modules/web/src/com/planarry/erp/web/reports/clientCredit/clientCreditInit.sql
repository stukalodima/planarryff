SELECT e.transportOwner.name AS transportOwner,
e.counterparty.name AS counterparty,
e.payDate AS payDate,
e.value AS value,
e.journey.journeyNumber AS journey
FROM erp$ClientCredit e