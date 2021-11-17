import React from 'react';
import StatelessWidget, {SIZE, TYPE} from "@/components/StatelessWidget";

function PageHome() {

    return (
        <StatelessWidget
            type={TYPE.PAGE}
            size={SIZE.LARGE}
            title="Welcome to the Telegram-Bot-Manager app!"
            description="Work in progress..."
        />
    );
}

export default PageHome;
