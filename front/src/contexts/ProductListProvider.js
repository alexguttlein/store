import React, { createContext, useContext, useState, useCallback} from 'react';
import { useApi } from './ApiProvider';
import { useUser } from './UserProvider';


const ProductContext = createContext();

export default function ProductListProvider({ children }) {
    const [productList, setProductList] = useState();
    const {isAdmin} = useUser();

    const [paginationState, setPaginationState] = useState();
    const {api} = useApi();

    const parseAttributes = (attributes) => {
        return attributes.reduce((acc, attr) => {
            if (attr.attributeName.toLowerCase() !== "stock") {
                acc[attr.attributeName] = [attr.value];
            }
            return acc;
        }, {});
    };

    const isProductDataValid = (product) => {
        return product &&
                product.alternative &&
                product.id != null &&
                product.stock != null &&
                product.alternative.id != null
    }

    const mapProductListData = useCallback((products) => {
        const validProducts = products.filter(product => isProductDataValid(product))

        const invalidProductsLength = products.length - validProducts.length;
    
        if (invalidProductsLength > 0) {
            console.warn(`Hay ${invalidProductsLength} productos que no están siendo mostrados debido a información inválida.`);
        }

        const productData =  validProducts.map((product, index) => ({
            id: product.id,
            alternativeId: product.alternative.id,
            name: product.alternative.alternativeName || "Sin nombre",
            photo: product.alternative.alternativePhoto || "",
            stock: product.stock,
            attributes: parseAttributes(product.attributes)
        }));
        
        if(!isAdmin())
            return productData.filter(x => (x.stock > 0))
        return productData
    }, [isAdmin]);

    const fetchProductListPage = useCallback(async (page, per_page) => {
        setProductList();
        setPaginationState();

        try {
            const response = await api.get(`/products?pageNo=${page}&pageSize=${per_page}`);
            
            if (!response.ok || !response.body.content) {
                setProductList(null);
                setPaginationState(null);
                console.error("Error al recuperar productos del server");
                return
            }

            setProductList(mapProductListData(response.body.content));
            
            const { pageable, totalPages } = response.body;

            if (pageable.pageNumber != null && totalPages != null) {
                setPaginationState({
                    page: pageable.pageNumber,
                    page_count: totalPages - 1,
                });
            } else {
                setPaginationState({
                    page: 0,
                    page_count: 0,
                });
                if (pageable !== "INSTANCE")
                    console.warn("Datos de paginación inválidos recibidos del servidor");
            }

        } catch (error) {
            setProductList(null);
            setPaginationState(null);
            console.error("Error al recuperar productos del server:", error);
            return
        }
    }, [api, mapProductListData]);

    return (
        <ProductContext.Provider value={{ productList, paginationState, fetchProductListPage }}>
            {children}
        </ProductContext.Provider>
    );
}

export const useProductList = () => {
    return useContext(ProductContext);
};
