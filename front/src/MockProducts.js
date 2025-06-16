export default class MockProducts {
    constructor() {
        this.productsList = []

        this.names = ["A", "B", "C", "D"]


        for (let index = 0; index < 50; index++) {
            this.productsList.push(
                {id:index, alternativeId:index%4, 
                    name: 'Producto ' + this.names[index%4] + ' ' + index, stock: index % 4 ? index: 0,
                    attributes:[]
                });
            
        }
        
        const color = ["Rojo", "Blanco", "Negro", "Azul"]
        const talle = "XL,L,M,S".split(",")
        const calidad = "Alta,Media,Baja".split(",")

        for (let index = 0; index < 50; index++) {
            this.productsList[index].attributes.push({attributeName: "color", value: color[(index + 2) % 4]})
            this.productsList[index].attributes.push({attributeName: "talle", value: talle[(index + 1) % 4]})
            this.productsList[index].attributes.push({attributeName: "calidad", value: calidad[(index) % 3]})
            this.productsList[index].attributes.push({attributeName: "stock", value: index%2})

        }
    }

    async getAllProducts() {
        return {data:this.productsList};
    }

    async getProductsPage(number, quantity) {
        const data = this.productsList.slice(number * quantity, Math.min(this.productsList.length, number * quantity + quantity ))
        return ({
            data: data, 
            pagination: {page: number, page_count: Math.ceil(this.productsList.length / quantity - 1)}
        });
    }

    async getProductsById(id) {
        const productData = {}
        productData["name"] = "Producto " + this.names[id%4]

        productData["variants"] = []
        
        this.productsList.forEach(product => {
            if (product.alternativeId !== Number(id))
                return 
            productData["variants"].push( {
                id: product["id"], 
                stock: product.attributes["stock"],
                attributes: product["attributes"].reduce((acc, v) => {
                    acc[v.attributeName] = v.value;
                    return acc;
                }, {})
            })
        })

        const attributesData = {}

        this.productsList.forEach(variant => {
            if (variant.alternativeId !== id)
                return 

            variant.attributes.forEach(attr => {
                if (attr.attributeName.toLowerCase() === "stock")
                    return;

                if(attributesData[attr.attributeName])
                    attributesData[attr.attributeName].push(attr.value)
                else 
                    attributesData[attr.attributeName] = [attr.value]
            })    
        });
        
        productData["attributes"] = attributesData
        
        return productData
    }

}