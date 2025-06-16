function isLessThanOneDayAgo(date) {
    const now = new Date(); // Current date and time
    const oneDayInMs = 24 * 60 * 60 * 1000; // Milliseconds in one day
    const givenDate = new Date(date); // Convert the input to a Date object
  
    return now - givenDate < oneDayInMs; // Check if the difference is less than one day
  }

const orderList = (x => {
    const statusEnum = ["CONFIRMED", "PROCESS", "SENT", "CANCELED"] 
    const l =[]

    for (let index = 0; index < 50; index++) {
        l.push(
            {id:index, owner:index%7, 
                status: statusEnum[index % 4],
                date: `${15}/11/2024`,
            });  
    }
    
    for (let index = 0; index < 50; index++) {
        l[index]["isCancelable"] =  isLessThanOneDayAgo(`11/${16}/2024`) && l[index]["status"] === "CONFIRMED"
    }

    return l
})()

export default class MockOrders {
    
    constructor() {
        this.orderList = orderList        
    }

    
    async getAllUserOrders(userId) {
        return {data:this.orderList.filter(x => x.owner === userId)};
    }

    async getAllAdminOrders() {
        return {data:this.orderList};
    }

    async getAdminOrdersPage(number, quantity) {
        const data = this.orderList.slice(number * quantity, Math.min(this.orderList.length, number * quantity + quantity ))
        return ({
            data: data, 
            pagination: {page: number, page_count: Math.ceil(this.orderList.length / quantity - 1)}
        });
    }

    async getUserOrdersPage(userId, number, quantity) {
        const data = this.orderList.filter(x => x.owner === userId).slice(number * quantity, Math.min(this.orderList.length, number * quantity + quantity ))
        return ({
            data: data, 
            pagination: {page: number, page_count: Math.ceil(this.orderList.filter(x => x.owner === userId).length / quantity - 1)}
        });
    }

}