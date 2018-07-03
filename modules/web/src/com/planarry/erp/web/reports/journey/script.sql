-- it is a Groovy script. Dataset named "Data".

import com.haulmont.cuba.core.global.AppBeans

def employeeService = AppBeans.get('erp_EmployeeService')
def persistence = AppBeans.get('cuba_Persistence')

def tx = persistence.createTransaction()
try {
    def result = []
    def queryStr = 'select e from erp$Journey e where e.status = 4 and e.manualJourney = false and e.employee in ?1 order by e.employee.name, e.startDate, e.endDate'

    def managers = employeeService.getEmployeesByUserCompany()

    if(managers && !managers.empty){
        def em = persistence.getEntityManager()
        def query = em.createQuery(queryStr)
        query.setParameter(1, managers)
        def journeyList = query.getResultList()

        journeyList.each { j ->
            result.add(['employeeName': j.employee.name,
                    'startDate': j.startDate,
                    'endDate': j.endDate,
                    'startAddress': j.startAddress,
                    'endAddress': j.endAddress,
                    'price': j.transportationPrice,
                    'distance': j.transportationDistance,
                    'rating': j.rating])
        }
    }
    return result
} finally {
    tx.end()
}