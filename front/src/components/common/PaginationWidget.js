import { useState, useEffect } from 'react';
import { Container } from 'react-bootstrap';
import Pagination from 'react-bootstrap/Pagination';


export default function PaginationWidget({pagination, loadPageNumber}) {
    
    const [page, setPage] = useState(pagination.page)
    let items = [];
    const visibleButtons = 5

    useEffect(() => {
        setPage(pagination.page);
    }, [pagination.page]);

    const handleOnClick = event => {  
        const pageNumber = Number(event.target.dataset.pageNumber);
        if (pageNumber >= 0 && pageNumber <= pagination.page_count) {
            setPage(pageNumber)
            loadPageNumber(pageNumber);
    
        }
    }
    
    const halfVisible = Math.floor(visibleButtons / 2);
    let startPage = Math.max(0, page - halfVisible);
    let endPage = Math.min(pagination.page_count, page + halfVisible);

    if (page <= halfVisible) {
        endPage = Math.min(pagination.page_count, visibleButtons - 1);
    } else if (page >= pagination.page_count - halfVisible) {
        startPage = Math.max(0, pagination.page_count - visibleButtons);
    }

    for (let number = startPage; number <= endPage; number++) {
        items.push(
            <Pagination.Item key={number} data-page-number={number} onClick={handleOnClick} active={number === page} >
            {number}
            </Pagination.Item>,
        );
    }

    return (
        <Container className="d-flex justify-content-center fixed-bottom">
            <Pagination className="justify-content-center">
                <Pagination.First
                    data-page-number={0}
                    onClick={handleOnClick}
                    disabled={page === 0}
                />
                <Pagination.Prev
                    data-page-number={page - 1}
                    onClick={handleOnClick}
                    disabled={page === 0}
                />
                
                {items}

                <Pagination.Next
                    data-page-number={page + 1}
                    onClick={handleOnClick}
                    disabled={page === pagination.page_count}
                />
                <Pagination.Last
                    data-page-number={pagination.page_count}
                    onClick={handleOnClick}
                    disabled={page === pagination.page_count}
                />
            </Pagination>
        </Container>
    );
}