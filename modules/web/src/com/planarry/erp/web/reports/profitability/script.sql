-- it is a Groovy script. Dataset named "Data".
import com.haulmont.cuba.core.global.AppBeans

def employeeService = AppBeans.get('erp_EmployeeService')
def persistence = AppBeans.get('cuba_Persistence')

def tx = persistence.createTransaction()
try {
    def result = []
    def queryStr = 'select e from erp$Journey e where e.status = 4 and e.manualJourney = false and e.employee in ?1 and e.createTs >= ?2 and e.createTs <= ?3 order by e.createTs'

    def managers = params['managers']
    if(!managers || managers.empty) {
        managers = employeeService.getEmployeesByUserCompany()
    }

    if(managers && !managers.empty){
    def em = persistence.getEntityManager()
    def query = em.createQuery(queryStr)
    query.setParameter(1, managers)
    query.setParameter(2, params['fromDate'])
    query.setParameter(3, params['toDate'])
    def journeyList = query.getResultList()

    journeyList.each { j ->
        def desPrice = 0

        j.journeyCargoes.each { c ->
             desPrice += c.cargo.desiredPrice
        }

        def delta = j.transportationPrice - desPrice

        result.add(['transport': j.transport.name,
                    'date': j.createTs,
                    'startDate': j.startDate,
                    'endDate': j.endDate,
                    'startAddress': j.startAddress,
                    'endAddress': j.endAddress,
                    'price': j.transportationPrice,
                    'desiredPrice': desPrice,
                    'delta': delta])
    }
    }
    return result
} finally {
    tx.end()
}