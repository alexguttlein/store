import Container from 'react-bootstrap/Container';
import Stack from 'react-bootstrap/Stack';
import FlashMessage from './FlashMessage';

export default function Body({ children }) {
    return (
        <Container>
            <Stack direction="horizontal" className="Body">
                <Container className="Content">
                    <FlashMessage />
                    {children}
                </Container>
            </Stack>
        </Container>
    );
}