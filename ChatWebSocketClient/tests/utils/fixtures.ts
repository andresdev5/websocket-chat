export const fixturesApi = {
    courses: {
        getAll: () => [{"id":12,"name":"Arquitectura de software","description":"test","createdAt":"2024-07-21 16:56:18","enrollments":[]},{"id":10,"name":"Aplicaciones distribuidas","description":"test","createdAt":"2024-07-20 15:56:45","updatedAt":"2024-07-20 15:56:54","enrollments":[{"id":6,"courseId":10,"userId":12,"status":"ENROLLED","enrolledAt":"2024-07-21 16:54:06"}]}]
    },
    users: {
        get: () => ({"id":1,"name":"John Doe","email":"johndoe@gmail.com","password":"12345"}),
        getAll: () => [{"id":1,"name":"John Doe","email":"johndoe@gmail.com","password":"12345"},{"id":11,"name":"Paul Doe","email":"pauldoe@gmail.com","password":"12345"},{"id":12,"name":"Anne Doe","email":"annedoe@gmail.com","password":"12345"}]
    }
}
