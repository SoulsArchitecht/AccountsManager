import React, { Component } from 'react';

class UrlList extends Component {
    constructor(props) {
        super(props)

        this.state = {
            urls: []
        }
    }
    render() {
        return (
            <div>
                <h2 className="text-center">Urls List</h2>
                <div className="row">
                    <table classname="table table-bordered border-primary">
                        <thead>
                            <tr>
                                <th>link</th>
                                <th>description</th>
                                <th>created at</th>
                                <th>changed at</th>
                                <th>login</th>
                                <th>password</th>
                                <th>email</th>
                                <th>emailAnother</th>
                                <th>nickname</th>
                                <th>active</th>
                                <th>actions</th>
                            </tr>    
                        </thead>

                        <tbody>
                            {
                                this.state.urls.map(
                                    url =>
                                    <tr key={url.id}>
                                        <td>{url.link}</td>
                                        <td>{url.description}</td>
                                        <td>{url.createdAt}</td>
                                        <td>{url.changedAt}</td>
                                        <td>{url.login}</td>
                                        <td>{url.password}</td>
                                        <td>{url.email}</td>
                                        <td>{url.emailAnother}</td>
                                        <td>{url.nickname}</td>
                                        <td>{url.active}</td>
                                        <td></td>

                                    </tr>
                                )
                            }
                        </tbody>    
                        
                    </table>    
                </div>               
            </div>
        );
    }
}

export default UrlList;